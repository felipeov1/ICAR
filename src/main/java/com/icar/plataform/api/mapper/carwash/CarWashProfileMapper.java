package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.plataform.api.dto.response.carwash.profile.WeeklyScheduleResponse;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.plataform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.plataform.domain.model.carwash.profile.SpecialDay;
import com.icar.plataform.domain.model.carwash.profile.WeeklySchedule;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarWashProfileMapper {
    CarWashProfileResponse toDto(CarWashProfile carWashProfile);
    CarWashProfile toEntity(CarWashProfileRequest carWashProfileRequest);

    WeeklyScheduleResponse toWeeklyScheduleDto(WeeklySchedule weeklySchedule);
    SpecialDayResponse toSpecialDayDto(SpecialDay specialDay);

    List<WeeklyScheduleResponse> toWeeklyScheduleDtos(List<WeeklySchedule> weeklySchedules);
    List<SpecialDayResponse> toSpecialDayDtos(List<SpecialDay> specialDays);
}
