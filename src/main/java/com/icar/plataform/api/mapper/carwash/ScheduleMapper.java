package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.plataform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.plataform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.plataform.api.dto.response.carwash.profile.WeeklyScheduleResponse;
import com.icar.plataform.domain.enums.SpecialDayStatus;
import com.icar.plataform.domain.model.carwash.profile.SpecialDay;
import com.icar.plataform.domain.model.carwash.profile.WeeklySchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    // Weekly Schedules
    WeeklySchedule toEntity(WeeklyScheduleRequest request);
    WeeklyScheduleResponse toResponse(WeeklySchedule entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    void updateWeeklyScheduleFromRequest(WeeklyScheduleRequest request, @MappingTarget WeeklySchedule entity);

    // Special Days
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "active", ignore = true) // Active is managed separately
    SpecialDay toEntity(SpecialDayRequest request);

    @Mapping(target = "isActive", source = "active")
    SpecialDayResponse toResponse(SpecialDay entity);

    void updateSpecialDayFromRequest(SpecialDayRequest request, @MappingTarget SpecialDay entity);


    // Conversões de tipo
    default LocalTime map(String time) {
        return time != null ? LocalTime.parse(time) : null;
    }

    default String map(LocalTime time) {
        return time != null ? time.toString() : null;
    }

}

