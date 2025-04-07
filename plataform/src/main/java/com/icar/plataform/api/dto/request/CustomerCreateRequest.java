package com.icar.plataform.api.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerCreateRequest(
        @Schema(description = "Nome completo do cliente", example = "Felipe Ferreira")
        @NotBlank String fullName,
        @Schema(description = "Email do cliente", example = "ofelipe439@gmail.com")
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,15}$") String phone,
        @NotBlank String password
) {}