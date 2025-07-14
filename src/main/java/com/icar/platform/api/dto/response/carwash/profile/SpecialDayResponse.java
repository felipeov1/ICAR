package com.icar.platform.api.dto.response.carwash.profile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record SpecialDayResponse(
        UUID id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        boolean isActive
) {}