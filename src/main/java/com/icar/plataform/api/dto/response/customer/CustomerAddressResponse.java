package com.icar.plataform.api.dto.response.customer;

import java.util.UUID;

public record CustomerAddressResponse(
        UUID id,
        String addressName,
        String zipCode,
        String city,
        String state,
        String street,
        String number,
        String additionalInstructions
) {}
