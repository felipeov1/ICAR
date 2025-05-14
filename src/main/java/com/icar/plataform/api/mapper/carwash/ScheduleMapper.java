package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.plataform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.plataform.domain.model.carwash.profile.SpecialDay;
import com.icar.plataform.domain.model.carwash.profile.WeeklySchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Named("toWeeklyScheduleEntity")
    WeeklySchedule toEntity(WeeklyScheduleRequest request);

    @Named("toSpecialDayEntity")
    SpecialDay toEntity(SpecialDayRequest request);

    default DayOfWeek mapDayOfWeek(String value) {
        return DayOfWeek.valueOf(value.toUpperCase());
    }

    default LocalTime mapTime(String time) {
        return time != null ? LocalTime.parse(time) : null;
    }

    default LocalDate mapDate(String date) {
        return LocalDate.parse(date);
    }
}