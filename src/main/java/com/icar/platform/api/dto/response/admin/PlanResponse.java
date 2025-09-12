package com.icar.platform.api.dto.response.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public record PlanResponse(
        UUID id,
        String name,
        BigDecimal price,
        int billingFrequency,
        ChronoUnit billingPeriod,
        Integer durationInDays,
        LocalDateTime createdAt
) {}