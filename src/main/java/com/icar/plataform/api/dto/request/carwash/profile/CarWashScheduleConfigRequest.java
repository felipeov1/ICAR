package com.icar.plataform.api.dto.request.carwash.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarWashScheduleConfigRequest {
    private List<WeeklyScheduleRequest> weeklySchedules;
    private List<SpecialDayRequest> specialDays;
}
