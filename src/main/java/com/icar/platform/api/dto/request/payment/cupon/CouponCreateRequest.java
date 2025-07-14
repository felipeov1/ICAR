package com.icar.platform.api.dto.request.payment.cupon;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponCreateRequest(
        @NotNull String code,
        UUID profileId,
        BigDecimal discountValue,
        BigDecimal discountPercentage,
        @NotNull LocalDateTime validFrom,
        @NotNull LocalDateTime validUntil,
        Integer maxUses,
        Integer maxUsesPerUser,
        BigDecimal minOrderValue
) {}