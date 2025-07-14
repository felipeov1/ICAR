package com.icar.platform.api.dto.request.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank @Size(min = 3, max = 100) String fullName,
        @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,15}$") String phone
) {}