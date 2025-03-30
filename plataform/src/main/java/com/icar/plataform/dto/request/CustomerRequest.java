package com.icar.plataform.dto.request;

import jakarta.validation.constraints.*;

public record CustomerRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,15}$") String phone,
        @NotBlank String password
) {}