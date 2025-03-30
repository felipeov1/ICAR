package com.icar.plataform.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CarWashRequest(
        @NotBlank String legalName,
        @NotBlank String cnpj,
        @NotBlank String address,
        @NotBlank String name,
        @NotBlank String phone,
        @NotBlank @Email String email,
        @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitude,
        @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitude
) {}