package com.icar.platform.application.service.admin.dashboard;

import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.enums.CreationChannel;
import com.icar.platform.domain.enums.SubscriptionStatus;
import com.icar.platform.domain.model.admin.MonthlyPlatformStats;
import com.icar.platform.domain.model.admin.Plan;
import com.icar.platform.domain.model.admin.Subscription;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.repository.admin.MonthlyPlatformStatsRepository;
import com.icar.platform.domain.repository.admin.SubscriptionRepository;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonthlySnapshotService {

    private final CarWashAppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MonthlyPlatformStatsRepository monthlyStatsRepository;

    private static final ZoneId BRASILIA_ZONE_ID = ZoneId.of("America/Sao_Paulo");

    @Scheduled(cron = "0 0 1 1 * ?")
    @Transactional
    public void captureAndSaveMonthlyStats() {
        YearMonth lastMonth = YearMonth.now(BRASILIA_ZONE_ID).minusMonths(1);
        log.info("Iniciando captura de estatísticas para o mês: {}", lastMonth);

        LocalDateTime startDate = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = lastMonth.atEndOfMonth().atTime(23, 59, 59);

        monthlyStatsRepository.findByYearAndMonth(lastMonth.getYear(), lastMonth.getMonthValue())
                .ifPresent(existing -> {
                    log.warn("Snapshot para {} já existe. Pulando execução.", lastMonth);
                });

        MonthlyPlatformStats stats = new MonthlyPlatformStats();
        stats.setYear(lastMonth.getYear());
        stats.setMonth(lastMonth.getMonthValue());

        List<CarWashAppointment> completedAppointments = appointmentRepository.findByStatusAndCreatedAtBetween(AppointmentStatus.COMPLETED, startDate, endDate);
        BigDecimal marketplaceGmv = completedAppointments.stream()
                .filter(a -> a.getCreationChannel() == CreationChannel.MARKETPLACE)
                .map(CarWashAppointment::getAmountPaid).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal manualGmv = completedAppointments.stream()
                .filter(a -> a.getCreationChannel() == CreationChannel.MANUAL)
                .map(CarWashAppointment::getAmountPaid).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.setGmvMarketplace(marketplaceGmv);
        stats.setGmvManual(manualGmv);
        stats.setGmvTotal(marketplaceGmv.add(manualGmv));

        List<Subscription> activeSubscriptionsAtEndOfMonth = subscriptionRepository.findActiveSubscriptionsAt(endDate);
        BigDecimal totalMrr = activeSubscriptionsAtEndOfMonth.stream()
                .map(sub -> calculateMonthlyValue(sub.getPlan()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<Subscription> newSubscriptionsInMonth = subscriptionRepository.findByStatusAndStartDateBetween(SubscriptionStatus.ACTIVE, startDate, endDate);
        BigDecimal newMrr = newSubscriptionsInMonth.stream()
                .map(sub -> calculateMonthlyValue(sub.getPlan()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.setMrrTotal(totalMrr);
        stats.setMrrNew(newMrr);
        stats.setActiveSubscriptions(activeSubscriptionsAtEndOfMonth.size());

        long totalUsersAtEndOfMonth = customerRepository.countByCreatedAtBefore(endDate.plusNanos(1));
        long newUsersInMonth = customerRepository.countByCreatedAtBetween(startDate, endDate);
        stats.setTotalUsers((int) totalUsersAtEndOfMonth);
        stats.setNewUsers((int) newUsersInMonth);

        long totalAppointmentsCreated = appointmentRepository.countByCreatedAtBetween(startDate, endDate);
        long canceledAppointments = appointmentRepository.countByStatusInAndCreatedAtBetween(List.of(AppointmentStatus.CANCELED, AppointmentStatus.REFUND_PENDING), startDate, endDate);
        stats.setTotalAppointments((int) totalAppointmentsCreated);
        stats.setCompletedAppointments(completedAppointments.size());
        stats.setCanceledAppointments((int) canceledAppointments);

        monthlyStatsRepository.save(stats);
        log.info("Snapshot de estatísticas para {} salvo com sucesso.", lastMonth);
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