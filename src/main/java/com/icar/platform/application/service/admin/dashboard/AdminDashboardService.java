package com.icar.platform.application.service.admin.dashboard;

import com.icar.platform.api.dto.response.admin.DashboardStatsResponse;
import com.icar.platform.api.mapper.appointment.AppointmentMapper;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.enums.CreationChannel;
import com.icar.platform.domain.enums.SubscriptionStatus;
import com.icar.platform.domain.model.admin.MonthlyPlatformStats;
import com.icar.platform.domain.model.admin.Plan;
import com.icar.platform.domain.model.admin.Subscription;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.repository.admin.MonthlyPlatformStatsRepository;
import com.icar.platform.domain.repository.admin.SubscriptionRepository;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final CarWashAppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MonthlyPlatformStatsRepository monthlyStatsRepository;
    private final AppointmentMapper appointmentMapper;

    private static final ZoneId BRASILIA_ZONE_ID = ZoneId.of("America/Sao_Paulo");

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats(String month) {
        YearMonth selectedYearMonth;
        try {
            selectedYearMonth = YearMonth.parse(month);
        } catch (Exception e) {
            selectedYearMonth = YearMonth.now(BRASILIA_ZONE_ID);
        }

        LocalDateTime startDate = selectedYearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

        YearMonth previousMonth = selectedYearMonth.minusMonths(1);
        LocalDateTime previousMonthEndDate = previousMonth.atEndOfMonth().atTime(23, 59, 59);


        Optional<MonthlyPlatformStats> previousMonthStats = monthlyStatsRepository.findByYearAndMonth(previousMonth.getYear(), previousMonth.getMonthValue());

        List<CarWashAppointment> allAppointmentsInPeriod = appointmentRepository.findByDateTimeBetween(startDate, endDate);

        List<CarWashAppointment> completedAppointmentsInPeriod = allAppointmentsInPeriod.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .collect(Collectors.toList());

        DashboardStatsResponse response = new DashboardStatsResponse();
        response.setFunnel(buildFunnelStats(allAppointmentsInPeriod));
        response.setUsers(buildUserStats(startDate, endDate, previousMonthEndDate, previousMonthStats));
        response.setGmv(buildGmvStats(completedAppointmentsInPeriod, previousMonthStats));
        List<CarWashAppointment> allAppointmentsCreatedInPeriod = appointmentRepository.findByCreatedAtBetween(startDate, endDate);
        response.setAppointments(buildAppointmentStats(allAppointmentsCreatedInPeriod, completedAppointmentsInPeriod, startDate, previousMonthStats));
        response.setMrr(buildMrrStats(startDate, endDate, previousMonthStats));
        response.setPartnerPerformance(buildPartnerPerformanceStats(allAppointmentsInPeriod));
        response.setHistoricalStats(new DashboardStatsResponse.HistoricalStatsData());

        return response;
    }


    private DashboardStatsResponse.MrrStats buildMrrStats(LocalDateTime periodStartDate, LocalDateTime periodEndDate, Optional<MonthlyPlatformStats> previousMonthStats) {
        List<Subscription> activeSubscriptionsInPeriod = subscriptionRepository.findByStatusAndStartDateBefore(SubscriptionStatus.ACTIVE, periodEndDate);
        BigDecimal totalMrr = activeSubscriptionsInPeriod.stream()
                .map(sub -> calculateMonthlyValue(sub.getPlan()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Subscription> newSubscriptionsThisMonth = subscriptionRepository.findByStatusAndStartDateBetween(SubscriptionStatus.ACTIVE, periodStartDate, periodEndDate);
        BigDecimal newMrrThisMonth = newSubscriptionsThisMonth.stream()
                .map(sub -> calculateMonthlyValue(sub.getPlan()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMrrLastMonth = previousMonthStats.map(MonthlyPlatformStats::getMrrTotal).orElse(BigDecimal.ZERO);
        BigDecimal changeFromLastMonth = totalMrr.subtract(totalMrrLastMonth);

        double changePercent = calculatePercentageChange(totalMrrLastMonth.doubleValue(), totalMrr.doubleValue());

        DashboardStatsResponse.MrrStats mrrStats = new DashboardStatsResponse.MrrStats();
        mrrStats.setTotal(totalMrr);
        mrrStats.setNewThisMonth(newMrrThisMonth);
        mrrStats.setActiveSubscriptions(activeSubscriptionsInPeriod.size());
        mrrStats.setChangeFromLastMonth(changeFromLastMonth);
        mrrStats.setChangePercent(changePercent);

        return mrrStats;
    }

    private DashboardStatsResponse.UserStats buildUserStats(LocalDateTime currentPeriodStart, LocalDateTime currentPeriodEnd, LocalDateTime previousPeriodEnd, Optional<MonthlyPlatformStats> previousMonthStats) {
        long newUsersThisPeriod = customerRepository.countByCreatedAtBetween(currentPeriodStart, currentPeriodEnd);
        long totalUsersInPeriod = customerRepository.countByCreatedAtBefore(currentPeriodEnd);

        long newUsersLastPeriod = previousMonthStats.map(MonthlyPlatformStats::getNewUsers).orElse(0);
        long totalUsersAtStartOfPeriod = totalUsersInPeriod - newUsersThisPeriod;

        double newUsersChangePercent = calculatePercentageChange(newUsersLastPeriod, newUsersThisPeriod);
        double totalUsersChangePercent = calculatePercentageChange(totalUsersAtStartOfPeriod, totalUsersInPeriod);

        DashboardStatsResponse.UserStats userStats = new DashboardStatsResponse.UserStats();
        DashboardStatsResponse.UserStats.StatValue totalStat = new DashboardStatsResponse.UserStats.StatValue();
        totalStat.setValue((int) totalUsersInPeriod);
        totalStat.setChangePercent(totalUsersChangePercent);
        userStats.setTotal(totalStat);

        DashboardStatsResponse.UserStats.StatValue newThisMonthStat = new DashboardStatsResponse.UserStats.StatValue();
        newThisMonthStat.setValue((int) newUsersThisPeriod);
        newThisMonthStat.setChangePercent(newUsersChangePercent);
        userStats.setNewThisMonth(newThisMonthStat);

        return userStats;
    }

    @Transactional(readOnly = true)
    public List<DashboardStatsResponse.PartnerMonthlyStatsDto> getPartnerMonthlyStats(UUID partnerId) {
        List<AppointmentStatus> excludedStatuses = List.of(AppointmentStatus.CANCELED, AppointmentStatus.REFUND_PENDING);

        List<CarWashAppointment> validAppointments = appointmentRepository.findByProfileIdAndStatusNotInOrderByDateTimeDesc(partnerId, excludedStatuses);

        Map<YearMonth, List<CarWashAppointment>> groupedByMonth = validAppointments.stream()
                .collect(Collectors.groupingBy(a -> YearMonth.from(a.getDateTime())));

        List<DashboardStatsResponse.PartnerMonthlyStatsDto> monthlyStats = groupedByMonth.entrySet().stream()
                .map(entry -> {
                    YearMonth monthKey = entry.getKey();
                    List<CarWashAppointment> monthAppointments = entry.getValue();

                    DashboardStatsResponse.PartnerMonthlyStatsDto dto = new DashboardStatsResponse.PartnerMonthlyStatsDto();
                    dto.setMonth(monthKey.toString());
                    dto.setTotalAppointments(monthAppointments.size());
                    dto.setMarketplaceAppointments((int) monthAppointments.stream()
                            .filter(a -> a.getCreationChannel() == CreationChannel.MARKETPLACE).count());
                    dto.setManualAppointments((int) monthAppointments.stream()
                            .filter(a -> a.getCreationChannel() == CreationChannel.MANUAL).count());
                    return dto;
                })
                .sorted(Comparator.comparing(DashboardStatsResponse.PartnerMonthlyStatsDto::getMonth).reversed())
                .collect(Collectors.toList());

        return monthlyStats;
    }

    private List<DashboardStatsResponse.PartnerPerformanceDto> buildPartnerPerformanceStats(List<CarWashAppointment> appointments) {
        Map<CarWashProfile, List<CarWashAppointment>> groupedByPartner = appointments.stream()
                .filter(a -> a.getProfile() != null)
                .collect(Collectors.groupingBy(CarWashAppointment::getProfile));

        return groupedByPartner.entrySet().stream()
                .map(entry -> {
                    CarWashProfile partner = entry.getKey();
                    List<CarWashAppointment> partnerAppointments = entry.getValue();

                    List<CarWashAppointment> validAppointments = partnerAppointments.stream()
                            .filter(a -> a.getStatus() != AppointmentStatus.CANCELED && a.getStatus() != AppointmentStatus.REFUND_PENDING)
                            .toList();

                    DashboardStatsResponse.PartnerPerformanceDto dto = new DashboardStatsResponse.PartnerPerformanceDto();
                    dto.setPartnerId(partner.getId());
                    dto.setPartnerName(partner.getName());
                    dto.setTotalAppointments(validAppointments.size());
                    dto.setMarketplaceAppointments((int) validAppointments.stream()
                            .filter(a -> a.getCreationChannel() == CreationChannel.MARKETPLACE).count());
                    dto.setManualAppointments((int) validAppointments.stream()
                            .filter(a -> a.getCreationChannel() == CreationChannel.MANUAL).count());
                    return dto;
                })
                .sorted(Comparator.comparing(DashboardStatsResponse.PartnerPerformanceDto::getTotalAppointments).reversed())
                .collect(Collectors.toList());
    }

    private DashboardStatsResponse.AppointmentStats buildAppointmentStats(
            List<CarWashAppointment> allAppointments,
            List<CarWashAppointment> completedAppointments,
            LocalDateTime currentPeriodStart,
            Optional<MonthlyPlatformStats> previousMonthStats) {

        DashboardStatsResponse.AppointmentStats stats = new DashboardStatsResponse.AppointmentStats();

        LocalDateTime startOfDay = LocalDate.now(BRASILIA_ZONE_ID).atStartOfDay();
        long todayCount = appointmentRepository.countByCreatedAtBetween(startOfDay, LocalDateTime.now(BRASILIA_ZONE_ID));
        long yesterdayCount = appointmentRepository.countByCreatedAtBetween(startOfDay.minusDays(1), startOfDay);

        DashboardStatsResponse.AppointmentStats.StatValue todayStat = new DashboardStatsResponse.AppointmentStats.StatValue();
        todayStat.setValue((int) todayCount);
        todayStat.setChangePercent(calculatePercentageChange(yesterdayCount, todayCount));
        stats.setToday(todayStat);

        long thisMonthCount = allAppointments.size();
        long lastMonthCount = previousMonthStats.map(MonthlyPlatformStats::getTotalAppointments).orElse(0);

        DashboardStatsResponse.AppointmentStats.StatValue thisMonthStat = new DashboardStatsResponse.AppointmentStats.StatValue();
        thisMonthStat.setValue((int) thisMonthCount);
        thisMonthStat.setChangePercent(calculatePercentageChange(lastMonthCount, thisMonthCount));
        stats.setThisMonth(thisMonthStat);

        long marketplaceCompletedCount = completedAppointments.stream()
                .filter(a -> a.getCreationChannel() == CreationChannel.MARKETPLACE)
                .count();
        long manualCompletedCount = completedAppointments.stream()
                .filter(a -> a.getCreationChannel() == CreationChannel.MANUAL)
                .count();

        DashboardStatsResponse.AppointmentStats.Breakdown breakdown = new DashboardStatsResponse.AppointmentStats.Breakdown();
        breakdown.setMarketplace((int) marketplaceCompletedCount);
        breakdown.setManual((int) manualCompletedCount);
        stats.setBreakdown(breakdown);

        return stats;
    }

    private DashboardStatsResponse.GmvStats buildGmvStats(List<CarWashAppointment> completedAppointments, Optional<MonthlyPlatformStats> previousMonthStats) {
        BigDecimal marketplaceGmv = completedAppointments.stream()
                .filter(a -> a.getCreationChannel() == CreationChannel.MARKETPLACE)
                .map(CarWashAppointment::getAmountPaid)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal manualGmv = completedAppointments.stream()
                .filter(a -> a.getCreationChannel() == CreationChannel.MANUAL)
                .map(CarWashAppointment::getAmountPaid)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalGmv = marketplaceGmv.add(manualGmv);
        BigDecimal lastMonthGmv = previousMonthStats.map(MonthlyPlatformStats::getGmvTotal).orElse(BigDecimal.ZERO);

        BigDecimal changeFromLastMonth = totalGmv.subtract(lastMonthGmv);
        double changePercent = calculatePercentageChange(lastMonthGmv.doubleValue(), totalGmv.doubleValue());

        DashboardStatsResponse.GmvStats gmvStats = new DashboardStatsResponse.GmvStats();
        gmvStats.setMarketplace(marketplaceGmv);
        gmvStats.setManual(manualGmv);
        gmvStats.setTotal(totalGmv);
        gmvStats.setChangePercent(changePercent);
        gmvStats.setChangeFromLastMonth(changeFromLastMonth);

        return gmvStats;
    }

    private DashboardStatsResponse.FunnelStats buildFunnelStats(List<CarWashAppointment> appointments) {
        DashboardStatsResponse.FunnelStats funnel = new DashboardStatsResponse.FunnelStats();
        List<CarWashAppointment> pending = appointments.stream().filter(a -> a.getStatus() == AppointmentStatus.CONFIRMED).collect(Collectors.toList());
        List<CarWashAppointment> completed = appointments.stream().filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).collect(Collectors.toList());
        List<CarWashAppointment> canceled = appointments.stream().filter(a -> a.getStatus() == AppointmentStatus.CANCELED || a.getStatus() == AppointmentStatus.REFUND_PENDING).collect(Collectors.toList());
        funnel.setCreated(buildFunnelStage(appointments));
        funnel.setPending(buildFunnelStage(pending));
        funnel.setCompleted(buildFunnelStage(completed));
        funnel.setCanceled(buildFunnelStage(canceled));
        return funnel;
    }

    private DashboardStatsResponse.FunnelStage buildFunnelStage(List<CarWashAppointment> appointments) {
        DashboardStatsResponse.FunnelStage stage = new DashboardStatsResponse.FunnelStage();
        stage.setTotal(appointments.size());
        stage.setMarketplace((int) appointments.stream().filter(a -> a.getCreationChannel() == CreationChannel.MARKETPLACE).count());
        stage.setManual((int) appointments.stream().filter(a -> a.getCreationChannel() == CreationChannel.MANUAL).count());
        stage.setDetails(appointments.stream().map(appointmentMapper::toDetailItemDto).collect(Collectors.toList()));
        return stage;
    }

    private double calculatePercentageChange(long oldValue, long newValue) {
        if (oldValue == 0) return newValue > 0 ? 100.0 : 0.0;
        return ((double) (newValue - oldValue) / oldValue) * 100.0;
    }

    private double calculatePercentageChange(double oldValue, double newValue) {
        if (oldValue == 0) return newValue > 0 ? 100.0 : 0.0;
        return ((newValue - oldValue) / oldValue) * 100.0;
    }

    private BigDecimal calculateMonthlyValue(Plan plan) {
        if (plan.getPrice() == null || plan.getPrice().compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        double divisor = switch (plan.getBillingPeriod()) {
            case DAYS -> plan.getBillingFrequency() / 30.44;
            case WEEKS -> plan.getBillingFrequency() / 4.345;
            case MONTHS -> (double) plan.getBillingFrequency();
            case YEARS -> plan.getBillingFrequency() * 12.0;
            default -> 1.0;
        };
        if (divisor == 0) return BigDecimal.ZERO;
        return plan.getPrice().divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP);
    }
}