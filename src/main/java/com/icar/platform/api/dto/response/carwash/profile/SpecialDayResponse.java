package com.icar.platform.api.dto.response.carwash.profile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record SpecialDayResponse(
        UUID id,
        LocalDate startDate,
        LocalDate endDate,
        String description,
        boolean isClosed,
        LocalTime startTime,
        LocalTime endTime
) {}