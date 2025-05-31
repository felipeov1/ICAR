package com.icar.plataform.api.dto.response.carwash.profile;

import java.time.LocalDateTime;

public record TimeSlotResponse(
        LocalDateTime startTime,
        LocalDateTime endTime,
        String slotType
) {}