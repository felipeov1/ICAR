package com.icar.plataform.api.dto.response;

import com.icar.plataform.domain.enums.CarWashOfferingModality;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

public record ServiceResponse(
        UUID id,
        String name,
        String description,
        CarWashOfferingModality modality,
        BigDecimal price,
        Duration estimatedTime
) {}