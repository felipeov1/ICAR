package com.icar.plataform.api.dto.response;

public record CustomerResponse(
        java.util.UUID id,
        String name,
        String email,
        boolean emailVerified
) {}