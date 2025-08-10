package com.icar.platform.api.dto.request.appointment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record AppointmentRequest(
        @NotNull UUID carwashId,
        @NotEmpty List<UUID> offeringIds,
        @NotNull UUID addressId,
        @NotNull String vehicleType,
        @NotNull LocalDateTime startTime,
        String couponCode,
        @NotNull String paymentMethod
) {}