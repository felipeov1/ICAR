package com.icar.platform.api.dto.request.carwash;

public record CarWashRegistrationRequest(
        String cnpj,
        String cpf,
        String legalName,
        String ownerName,
        String phone,
        String email,
        String password,
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {}
