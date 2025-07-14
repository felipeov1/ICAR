package com.icar.platform.api.dto.response.customer;

import java.time.ZonedDateTime;
import java.util.UUID;

public record CustomerProfileResponse(
        UUID id,
        String fullName,
        String email,
        String phone,
        boolean emailVerified,
        ZonedDateTime createdAt,
        ZonedDateTime  updatedAt
) {}