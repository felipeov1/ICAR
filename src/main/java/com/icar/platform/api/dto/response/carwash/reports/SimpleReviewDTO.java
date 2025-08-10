package com.icar.platform.api.dto.response.carwash.reports;

import java.time.LocalDateTime;
import java.util.UUID;

public record SimpleReviewDTO(
        UUID id,
        Integer rating,
        LocalDateTime date,
        String comment
) {}