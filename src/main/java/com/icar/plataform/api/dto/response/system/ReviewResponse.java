package com.icar.plataform.api.dto.response.system;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        UUID customerId,
        UUID carWashId,
        UUID serviceId,
        Integer rating,
        String feedback,
        LocalDateTime createdAt
) {}