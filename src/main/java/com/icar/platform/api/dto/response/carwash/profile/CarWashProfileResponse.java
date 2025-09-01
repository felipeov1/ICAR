package com.icar.platform.api.dto.response.carwash.profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CarWashProfileResponse(
        UUID id,
        String name,
        String description,
        String logo,
        BigDecimal rating,
        Integer reviews,
        LocalDateTime createdAt,
        String[] modalities,
        String coverPhoto,
        String subdomain,
        String whatsapp,
        String[] locations,
        String observations,
        boolean mercadoPagoConnected,
        String mercadoPagoPublicKey
) {}