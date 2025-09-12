package com.icar.platform.api.dto.response.notification;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String type,
        String text,
        String appointmentTime,
        boolean read,
        Instant createdAt
) {}