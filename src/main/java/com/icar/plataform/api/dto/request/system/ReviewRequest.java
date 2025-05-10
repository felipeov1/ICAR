package com.icar.plataform.api.dto.request.system;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record ReviewRequest(
        @NotNull UUID customerId,
        @NotNull UUID carWashId,
        @NotNull UUID serviceId,
        @Min(1) @Max(5) Integer rating,
        String feedback
) {}