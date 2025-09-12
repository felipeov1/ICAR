package com.icar.platform.api.dto.request.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponRequest(
        String code,
        UUID profileId,
        BigDecimal discountValue,
        BigDecimal discountPercentage,
        LocalDateTime validFrom,
        LocalDateTime validUntil,
        Integer maxUses,
        Integer maxUsesPerUser,
        BigDecimal minOrderValue
) {}