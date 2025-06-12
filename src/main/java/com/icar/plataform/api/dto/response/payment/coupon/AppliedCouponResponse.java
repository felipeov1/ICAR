package com.icar.plataform.api.dto.response.payment.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppliedCouponResponse(
        UUID id,
        UUID couponId,
        String couponCode,
        UUID appointmentId,
        UUID customerId,
        BigDecimal originalAmount,
        BigDecimal finalAmount,
        BigDecimal discountApplied,
        LocalDateTime appliedAt
) {}