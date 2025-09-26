package com.icar.platform.api.dto.request.appointment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ManualAppointmentRequest(
        @NotNull @Valid CustomerPayload customer,
        @NotEmpty List<UUID> offeringIds,
        @NotNull String vehicleType,
        @NotNull LocalDateTime startTime
) {
    public record CustomerPayload(
            UUID id,
            @NotNull String name,
            String phone,
            String zipCode,
            String street,
            String number,
            String neighborhood,
            String city,
            String state,
            String additionalInstructions
    ) {}
}