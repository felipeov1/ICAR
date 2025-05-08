package com.icar.plataform.api.dto.request;

import com.icar.plataform.domain.enums.CarWashOfferingModality;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CarWashOfferingRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 500) String description,
        @NotNull @Positive BigDecimal price,
        @NotNull @Min(5) @Max(240) Integer durationMinutes,
        @NotNull CarWashOfferingModality modality
) {}
