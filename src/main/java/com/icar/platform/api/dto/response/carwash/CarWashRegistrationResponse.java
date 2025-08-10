package com.icar.platform.api.dto.response.carwash;

import java.util.UUID;

public record CarWashRegistrationResponse(
        UUID id,
        String cnpj,
        String cpf,
        String legalName,
        String tradeName,
        String ownerName,
        String phone,
        String email,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {}
