package com.icar.plataform.api.dto.response;

import com.icar.plataform.domain.enums.ServiceModality;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

public record ServiceResponse(
        UUID id,
        String name,
        String description,
        ServiceModality modality,
        BigDecimal price,
        Duration estimatedTime
) {}