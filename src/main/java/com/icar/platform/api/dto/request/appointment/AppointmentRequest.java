package com.icar.platform.api.dto.request.appointment;

import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record AppointmentRequest(
        @NotNull UUID carwashId,
        @NotNull UUID offeringId,
        List<UUID> extraOfferingIds,
        @NotNull UUID addressId,
        @NotNull String vehicleType,
        @NotNull ZonedDateTime startTime,
        String couponCode,
        @NotNull String paymentMethod
) {}