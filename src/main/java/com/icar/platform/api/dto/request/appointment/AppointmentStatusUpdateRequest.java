package com.icar.platform.api.dto.request.appointment;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AppointmentStatusUpdateRequest(
        @NotNull
        UUID appointmentId,
        @NotNull
        UUID carWashId,
        String notes,
        BigDecimal amountPaid
) {}