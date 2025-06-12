package com.icar.plataform.api.dto.response.appointment;

import com.icar.plataform.domain.enums.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentConfirmationResponse(
        UUID appointmentId,
        UUID carWashId,
        AppointmentStatus previousStatus,
        AppointmentStatus newStatus,
        LocalDateTime confirmedAt,
        String message
) {}