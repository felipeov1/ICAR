package com.icar.plataform.api.dto.response.carwash.profile;

import java.util.UUID;

public record SpecialDayResponse(
        UUID id,
        String date,
        String startTime,
        String endTime,
        boolean isHoliday,
        String reason
) {}
