package com.icar.platform.api.dto.response.carwash.profile;

import java.time.LocalDateTime;

public record ReviewResponse(
        String customerName,
        int rating,
        String comment,
        LocalDateTime createdAt
) {}
