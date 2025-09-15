package com.icar.platform.api.dto.response.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public record CouponRecentUsageResponse(
        UUID id,
        String customerName,
        String couponCode,
        String partnerName,
        ZonedDateTime usedAt,
        BigDecimal discountAmount
) {}