package com.icar.plataform.api.dto.request.carwash.profile;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;

@Getter
@Setter
public class WeeklyScheduleRequest {
    private DayOfWeek dayOfWeek;
    private String startTime; // formato "HH:mm"
    private String endTime; // formato "HH:mm"
    private boolean available;
    private int appointmentIntervalMinutes;
}