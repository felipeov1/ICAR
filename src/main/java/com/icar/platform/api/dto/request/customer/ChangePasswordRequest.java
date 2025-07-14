package com.icar.platform.api.dto.request.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank String currentPassword,
        @NotBlank
        @Size(min = 8, max = 30)
        String newPassword,
        @NotBlank String confirmPassword
) {}