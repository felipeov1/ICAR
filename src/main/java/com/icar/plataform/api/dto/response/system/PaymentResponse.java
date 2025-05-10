package com.icar.plataform.api.dto.response.system;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        String paymentIntentId,
        String paymentStatus,
        BigDecimal amount,
        String pixCode, // Null se não for PIX
        Instant pixExpiration
) {}
