package com.icar.platform.api.dto.response.customer;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerAddressResponse(
        UUID id,
        String addressName,
        String zipCode,
        String city,
        String state,
        String street,
        String neighborhood,
        String streetNumber,
        String additionalInstructions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public String getShortDisplay() {
        return String.format("%s: %s, %s", addressName, street, streetNumber, neighborhood);
    }
}