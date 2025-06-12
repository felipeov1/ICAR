package com.icar.plataform.api.dto.response.system;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

public record ServiceResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Duration estimatedTime
) {}