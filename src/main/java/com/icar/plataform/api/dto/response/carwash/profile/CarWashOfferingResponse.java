package com.icar.plataform.api.dto.response.carwash.profile;

import com.icar.plataform.domain.enums.CarWashOfferingModality;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CarWashOfferingResponse(
        UUID id,
        @NotBlank @Size(max = 100) String name,
        @Size(max = 500) String description,
        @NotNull @Positive BigDecimal price,
        String formattedPrice,
        String formattedTime,
        Integer estimatedTime,
        @NotNull CarWashOfferingModality modality,
        String modalityText,
        Boolean active,
        String statusText
) {}
