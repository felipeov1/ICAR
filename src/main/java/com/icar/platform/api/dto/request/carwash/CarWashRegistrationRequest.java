package com.icar.platform.api.dto.request.carwash;

public record CarWashRegistrationRequest(
        String cnpj,
        String cpf,
        String tradeName,
        String ownerName,
        String phone,
        String email,
        String password,
        String city,
        String state,
        String zipCode
) {}