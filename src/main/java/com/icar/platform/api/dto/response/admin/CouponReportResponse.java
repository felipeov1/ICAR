package com.icar.platform.api.dto.response.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CouponReportResponse(
        LocalDate reportDate,
        BigDecimal grandTotalDiscount,
        List<PartnerReport> partners
) {
    public record PartnerReport(
            String partnerId,
            String partnerName,
            int totalCouponsUsed,
            BigDecimal totalDiscount,
            List<CouponUsageDetail> coupons
    ) {}

    public record CouponUsageDetail(
            String id,
            String customerName,
            String couponCode,
            BigDecimal discountAmount
    ) {}
}