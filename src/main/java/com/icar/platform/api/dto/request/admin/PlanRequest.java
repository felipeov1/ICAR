package com.icar.platform.api.dto.request.admin;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public record PlanRequest(
        String name,
        BigDecimal price,
        int billingFrequency,
        ChronoUnit billingPeriod,
        Integer durationInDays
) {}