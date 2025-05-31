package com.icar.plataform.api.dto.response.customer;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerAddressResponse(
        UUID id,
        String addressName,
        String zipCode,
        String city,
        String state,
        String street,
        String streetNumber,
        String additionalInstructions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public String getShortDisplay() {
        return String.format("%s: %s, %s", addressName, street, streetNumber);
    }
}