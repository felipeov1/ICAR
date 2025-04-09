package com.icar.plataform.api.dto.request;

import java.util.UUID;

public record NotificationRequest(
        UUID userId,
        String userType, // "CUSTOMER" ou "CAR_WASH"
        String message
) {}
