package com.icar.platform.api.dto.response.customer;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerProfileResponse(
        UUID id,
        String fullName,
        String email,
        String phone,
        String identificationNumber,
        boolean emailVerified,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}