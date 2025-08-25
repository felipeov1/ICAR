package com.icar.platform.application.service.carwash.reports;

import com.icar.platform.api.dto.response.carwash.reports.ReportDataResponse;
import com.icar.platform.api.dto.response.carwash.reports.ServiceCountDTO;
import com.icar.platform.api.dto.response.carwash.reports.SimpleReviewDTO;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.enums.CreationChannel;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.carwash.profile.Review;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.domain.repository.carwash.profile.ReviewRepository;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {

    private final CarWashAppointmentRepository appointmentRepository;
    private final CarWashProfileRepository profileRepository;
    private final ReviewRepository reviewRepository;
    private static final ZoneId SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo");

    @Override
    @Transactional(readOnly = true)
    public ReportDataResponse generateReport(UUID profileId, String periodStr) {
        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found with id: " + profileId));

        LocalDateTime now = LocalDateTime.now(SAO_PAULO_ZONE);

        DateRange currentRange = calculateDateRange(periodStr, now);
        DateRange previousRange = calculateDateRange(periodStr, now.minus(getPeriod(periodStr)));

        List<CarWashAppointment> currentAppointments = appointmentRepository
                .findAllByProfileIdAndStatusAndDateTimeBetween(profileId, AppointmentStatus.COMPLETED, currentRange.start(), currentRange.end());

        List<Review> currentReviews = reviewRepository
                .findByProfileIdAndCreatedAtBetweenOrderByCreatedAtDesc(profileId, currentRange.start(), currentRange.end());

        List<CarWashAppointment> previousAppointments = appointmentRepository
                .findAllByProfileIdAndStatusAndDateTimeBetween(profileId, AppointmentStatus.COMPLETED, previousRange.start(), previousRange.end());

        BigDecimal totalRevenue = calculateTotalRevenue(currentAppointments);
        BigDecimal revenueOnline = calculateRevenueByChannel(currentAppointments, CreationChannel.MARKETPLACE);
        BigDecimal revenueLocal = calculateRevenueByChannel(currentAppointments, CreationChannel.MANUAL);

        long servicesMarketplace = countServicesByChannel(currentAppointments, CreationChannel.MARKETPLACE);
        long servicesManual = countServicesByChannel(currentAppointments, CreationChannel.MANUAL);

        ReportDataResponse.ServicesCompleted servicesCompleted = ReportDataResponse.ServicesCompleted.builder()
                .total((int) (servicesMarketplace + servicesManual))
                .marketplace((int) servicesMarketplace)
                .manual((int) servicesManual)
                .build();

        int servicesChange = currentAppointments.size() - previousAppointments.size();

        List<ServiceCountDTO> topServices = calculateTopServices(currentAppointments);
        List<SimpleReviewDTO> recentReviews = mapToSimpleReviews(currentReviews);

        return ReportDataResponse.builder()
                .totalRevenue(totalRevenue)
                .revenueOnline(revenueOnline)
                .revenueLocal(revenueLocal)
                .servicesCompleted(servicesCompleted)
                .servicesChange(servicesChange)
                .averageRating(profile.getRating())
                .totalReviews(profile.getReviews())
                .topServices(topServices)
                .recentReviews(recentReviews)
                .build();
    }

    private BigDecimal calculateTotalRevenue(List<CarWashAppointment> appointments) {
        return appointments.stream().map(CarWashAppointment::getAmountPaid).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private BigDecimal calculateRevenueByChannel(List<CarWashAppointment> appointments, CreationChannel channel) {
        return appointments.stream().filter(a -> a.getCreationChannel() == channel).map(CarWashAppointment::getAmountPaid).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private long countServicesByChannel(List<CarWashAppointment> appointments, CreationChannel channel) {
        return appointments.stream().filter(a -> a.getCreationChannel() == channel).count();
    }
    private List<ServiceCountDTO> calculateTopServices(List<CarWashAppointment> appointments) {
        return appointments.stream().flatMap(appointment -> appointment.getSelectedServices().stream()).map(offering -> offering.getName()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().map(entry -> new ServiceCountDTO(entry.getKey(), entry.getValue())).sorted(Comparator.comparingLong(ServiceCountDTO::count).reversed()).limit(2).collect(Collectors.toList());
    }
    private List<SimpleReviewDTO> mapToSimpleReviews(List<Review> reviews) {
        return reviews.stream().map(review -> new SimpleReviewDTO(review.getId(), review.getRating(), review.getCreatedAt(), review.getComment())).collect(Collectors.toList());
    }

    private DateRange calculateDateRange(String period, LocalDateTime anchorDate) {
        LocalDateTime start, end;
        LocalDate anchorLocalDate = anchorDate.toLocalDate();

        switch (period.toUpperCase()) {
            case "YEAR":
                start = anchorLocalDate.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
                end = anchorLocalDate.atTime(LocalTime.MAX);
                break;
            case "MONTH":
                start = anchorLocalDate.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
                end = anchorLocalDate.atTime(LocalTime.MAX);
                break;
            case "WEEK":
            default:
                start = anchorLocalDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).atStartOfDay();
                end = anchorLocalDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).atTime(LocalTime.MAX);
                break;
        }
        return new DateRange(start, end);
    }

    private Period getPeriod(String periodStr) {
        switch (periodStr.toUpperCase()) {
            case "YEAR": return Period.ofYears(1);
            case "MONTH": return Period.ofMonths(1);
            case "WEEK":
            default: return Period.ofWeeks(1);
        }
    }
    private record DateRange(LocalDateTime start, LocalDateTime end) {}
}