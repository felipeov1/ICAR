package com.icar.platform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.NotBlank;

public record CompanyCustomerRequest(
        @NotBlank String fullName,
        String phone,
        String zipCode,
        String street,
        String streetNumber,
        String neighborhood,
        String city,
        String state,
        String additionalInstructions
) {}
