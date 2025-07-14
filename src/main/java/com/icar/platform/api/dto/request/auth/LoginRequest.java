package com.icar.platform.api.dto.request.auth;

public record LoginRequest(
        String email,
        String password,
        boolean rememberMe
) {}