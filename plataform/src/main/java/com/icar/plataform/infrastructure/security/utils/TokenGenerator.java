package com.icar.plataform.infrastructure.security.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TokenGenerator {
    public String generateToken() {
        // Gera um UUID aleatório como token
        return UUID.randomUUID().toString();
    }

    // Opcional: metodo para gerar token com expiração
    public String generateTokenWithExpiration(int days) {
        return UUID.randomUUID().toString() + "|" +
               LocalDateTime.now().plusDays(days).toString();
    }

}
