package com.icar.plataform.api.dto.request.auth;

public record LoginRequest(
        String email,
        String password,
        boolean rememberMe
) {}