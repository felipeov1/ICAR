package com.icar.plataform.api.dto.response.customer;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerProfileResponse(
        UUID id,
        String fullName,
        String email,
        String phone,
        boolean emailVerified,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}