package com.icar.plataform.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CarWashResponse(
        UUID id,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        Double averageRating
) {}