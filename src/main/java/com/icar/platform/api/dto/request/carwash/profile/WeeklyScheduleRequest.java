package com.icar.platform.api.dto.request.carwash.profile;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;

@Getter
@Setter
public class WeeklyScheduleRequest {
    private DayOfWeek dayOfWeek;
    private String startTime;
    private String endTime;
    private boolean available;
    private int appointmentIntervalMinutes;
}