package com.icar.platform.application.service.admin.coupon;

import com.icar.platform.api.dto.response.admin.CouponRecentUsageResponse;
import com.icar.platform.api.dto.response.admin.CouponReportResponse;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.coupon.AppliedCoupon;
import com.icar.platform.domain.repository.admin.AppliedCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final AppliedCouponRepository appliedCouponRepository;
    private static final ZoneId BRASILIA_ZONE_ID = ZoneId.of("America/Sao_Paulo");

    @Transactional(readOnly = true)
    public List<CouponRecentUsageResponse> getRecentCouponUsages() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "appointment.updatedAt"));

        return appliedCouponRepository.findAll(pageRequest).getContent().stream()
                .filter(ac -> ac.getAppointment() != null && ac.getAppointment().getStatus() == AppointmentStatus.COMPLETED)
                .map(this::toRecentUsageResponse)
                .collect(Collectors.toList());
    }

    public CouponReportResponse generateCouponReport(LocalDate date, String partnerId) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<AppliedCoupon> usages = "all".equalsIgnoreCase(partnerId)
                ? appliedCouponRepository.findByAppointmentUpdatedAtBetween(startOfDay, endOfDay)
                : appliedCouponRepository.findByAppointmentUpdatedAtBetweenAndPartnerId(startOfDay, endOfDay, UUID.fromString(partnerId));

        List<AppliedCoupon> completedUsages = usages.stream()
                .filter(ac -> ac.getAppointment() != null && ac.getAppointment().getStatus() == AppointmentStatus.COMPLETED)
                .toList();

        Map<UUID, List<AppliedCoupon>> groupedByPartner = completedUsages.stream()
                .filter(ac -> ac.getAppointment().getProfile() != null)
                .collect(Collectors.groupingBy(ac -> ac.getAppointment().getProfile().getId()));

        List<CouponReportResponse.PartnerReport> partnerReports = groupedByPartner.entrySet().stream()
                .map(entry -> {
                    UUID pId = entry.getKey();
                    List<AppliedCoupon> partnerUsages = entry.getValue();

                    if (partnerUsages.isEmpty()) {
                        return null;
                    }

                    String partnerName = partnerUsages.getFirst().getAppointment().getProfile().getCarWashRegistration().getTradeName();

                    BigDecimal totalDiscount = partnerUsages.stream()
                            .map(AppliedCoupon::getDiscountApplied)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    List<CouponReportResponse.CouponUsageDetail> couponDetails = partnerUsages.stream()
                            .map(ac -> new CouponReportResponse.CouponUsageDetail(
                                    ac.getId().toString(),
                                    ac.getCustomer().getFullName(),
                                    ac.getCoupon().getCode(),
                                    ac.getDiscountApplied()
                            ))
                            .collect(Collectors.toList());

                    return new CouponReportResponse.PartnerReport(
                            pId.toString(),
                            partnerName,
                            partnerUsages.size(),
                            totalDiscount,
                            couponDetails
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        BigDecimal grandTotal = partnerReports.stream()
                .map(CouponReportResponse.PartnerReport::totalDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CouponReportResponse(date, grandTotal, partnerReports);
    }

    private CouponRecentUsageResponse toRecentUsageResponse(AppliedCoupon ac) {
        LocalDateTime completionTimeLocal = ac.getAppointment().getUpdatedAt();

        ZonedDateTime completionTimeZoned = completionTimeLocal.atZone(BRASILIA_ZONE_ID);

        return new CouponRecentUsageResponse(
                ac.getId(),
                ac.getCustomer().getFullName(),
                ac.getCoupon().getCode(),
                ac.getAppointment().getProfile().getCarWashRegistration().getTradeName(),
                completionTimeZoned,
                ac.getDiscountApplied()
        );
    }
}