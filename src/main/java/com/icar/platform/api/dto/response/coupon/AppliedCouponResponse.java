package com.icar.platform.api.dto.response.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
        ZonedDateTime appliedAt
) {}