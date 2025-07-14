package com.icar.platform.api.dto.response.payment.coupon;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record CouponResponse(
        UUID id,
        String code,
        UUID profileId,
        BigDecimal discountValue,
        BigDecimal discountPercentage,
        ZonedDateTime validFrom,
        ZonedDateTime validUntil,
        Integer maxUses,
        Integer maxUsesPerUser,
        Integer currentUses,
        BigDecimal minOrderValue
) {}