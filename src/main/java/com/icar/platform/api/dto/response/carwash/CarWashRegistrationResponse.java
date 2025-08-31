package com.icar.platform.api.dto.response.carwash;

import java.util.UUID;

public record CarWashRegistrationResponse(
        UUID id,
        String cnpj,
        String tradeName,
        String ownerName,
        String phone,
        String email,
        String city,
        String state,
        String zipCode
) {}