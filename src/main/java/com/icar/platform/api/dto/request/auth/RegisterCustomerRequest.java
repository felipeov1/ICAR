package com.icar.platform.api.dto.request.auth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterCustomerRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,15}$") String phone,
        @NotBlank String password
) {}