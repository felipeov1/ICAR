package com.icar.platform.api.dto.request.carwash;

import jakarta.validation.constraints.NotBlank;

public record CarWashLoginRequest(
        @NotBlank String email,
        @NotBlank String password
) {}