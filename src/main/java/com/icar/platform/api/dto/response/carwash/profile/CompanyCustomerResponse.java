package com.icar.platform.api.dto.response.carwash.profile;

import java.util.UUID;

public record CompanyCustomerResponse(
        UUID id,
        String name,
        String phone,
        String zipCode,
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String additionalInstructions
) {}
