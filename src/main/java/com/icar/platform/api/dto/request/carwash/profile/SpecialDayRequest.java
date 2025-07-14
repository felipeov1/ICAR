package com.icar.platform.api.dto.request.carwash.profile;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class SpecialDayRequest {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}