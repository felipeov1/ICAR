package com.icar.plataform.api.dto.request;

public record LoginRequest(
        String email,
        String password,
        boolean rememberMe
) {}