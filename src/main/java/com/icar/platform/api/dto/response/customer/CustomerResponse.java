package com.icar.platform.api.dto.response.customer;

public record CustomerResponse(
        java.util.UUID id,
        String name,
        String email,
        boolean emailVerified
) {}