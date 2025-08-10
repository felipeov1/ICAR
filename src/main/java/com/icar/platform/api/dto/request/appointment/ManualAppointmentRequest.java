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
            @NotNull String phone,
            @NotNull String zipCode,
            @NotNull String street,
            @NotNull String number,
            @NotNull String neighborhood,
            @NotNull String city,
            @NotNull String state,
            String additionalInstructions
    ) {}
}