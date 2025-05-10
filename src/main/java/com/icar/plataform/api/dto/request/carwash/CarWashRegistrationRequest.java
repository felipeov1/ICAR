package com.icar.plataform.api.dto.request.carwash;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record CarWashRegistrationRequest(
        String cnpj,
        String cpf,
        @NotBlank String legal_name,
        @NotBlank String trade_name,
        @NotBlank String ownerName,
        @NotBlank String phone,
        @NotBlank @Email String email,
        @NotBlank String address
) {
}