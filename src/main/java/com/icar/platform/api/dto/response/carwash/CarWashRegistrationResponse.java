package com.icar.platform.api.dto.response.carwash;

import java.util.UUID;

public record CarWashRegistrationResponse(
        UUID id,
        String cnpj,
        String legalName,
        String ownerName,
        String phone,
        String email,
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {}