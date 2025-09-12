package com.icar.platform.api.dto.response.admin;

import com.icar.platform.api.dto.response.admin.PartnerSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
        UUID id,
        String code,
        PartnerSummaryResponse profile,
        BigDecimal discountValue,
        BigDecimal discountPercentage,
        LocalDateTime validFrom,
        LocalDateTime validUntil,
        Integer maxUses,
        Integer maxUsesPerUser,
        Integer currentUses,
        LocalDateTime createdAt
) {}