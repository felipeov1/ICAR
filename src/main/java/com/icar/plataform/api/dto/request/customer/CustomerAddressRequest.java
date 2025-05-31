package com.icar.plataform.api.dto.request.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerAddressRequest(
        @NotBlank
        @Size(max = 100)
        String addressName,

        @NotBlank
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Invalid zip code format")
        String zipCode,

        @NotBlank
        @Size(max = 100)
        String city,

        @NotBlank
        @Size(max = 50)
        String state,

        @NotBlank
        @Size(max = 255)
        String street,

        @NotBlank
        @Size(max = 20)
        String streetNumber,

        @Size(max = 255)
        String additionalInstructions
) {}