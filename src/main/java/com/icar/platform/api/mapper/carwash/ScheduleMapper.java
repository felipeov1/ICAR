package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.platform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.platform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.platform.api.dto.response.carwash.profile.WeeklyScheduleResponse;
import com.icar.platform.domain.model.carwash.profile.SpecialDay;
import com.icar.platform.domain.model.carwash.profile.WeeklySchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalTime;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    WeeklySchedule toEntity(WeeklyScheduleRequest request);
    WeeklyScheduleResponse toResponse(WeeklySchedule entity);

    @Mapping(target = "id", ignore = true)
    void updateWeeklyScheduleFromRequest(WeeklyScheduleRequest request, @MappingTarget WeeklySchedule entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    SpecialDay toEntity(SpecialDayRequest request);

    @Mapping(target = "isActive", source = "active")
    SpecialDayResponse toResponse(SpecialDay entity);

    void updateSpecialDayFromRequest(SpecialDayRequest request, @MappingTarget SpecialDay entity);

    default LocalTime map(String time) {
        return time != null ? LocalTime.parse(time) : null;
    }

    default String map(LocalTime time) {
        return time != null ? time.toString() : null;
    }

}

