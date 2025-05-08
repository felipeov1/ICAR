package com.icar.plataform.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CustomerAddressRequest(
        @NotBlank String addressName,
        @NotBlank String zipCode,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String street,
        @NotBlank String number,
        String additionalInstructions
) {}
