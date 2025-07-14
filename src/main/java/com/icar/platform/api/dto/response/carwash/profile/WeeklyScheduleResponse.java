package com.icar.platform.api.dto.response.carwash.profile;

import java.time.DayOfWeek;
import java.util.UUID;

public record WeeklyScheduleResponse(
    UUID id,
    DayOfWeek dayOfWeek,
    String startTime,
    String endTime,
    boolean available,
    int appointmentIntervalMinutes
) {}