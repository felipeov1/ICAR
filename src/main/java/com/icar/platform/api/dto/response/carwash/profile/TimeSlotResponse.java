package com.icar.platform.api.dto.response.carwash.profile;

import java.time.ZonedDateTime;

public record TimeSlotResponse(
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        String slotType
) {}