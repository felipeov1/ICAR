package com.icar.plataform.api.dto.request.appointment;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AppointmentStatusUpdateRequest(
        @NotNull(message = "ID do agendamento é obrigatório")
        UUID appointmentId,

        @NotNull(message = "ID do lava-rápido é obrigatório")
        UUID carWashId,

        String notes,

        BigDecimal amountPaid
) {}