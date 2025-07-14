package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.AppointmentConfigRequest;
import com.icar.platform.api.dto.response.carwash.profile.AppointmentConfigResponse;
import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentConfigMapper {

    @Mapping(source = "maxAdvanceBookingDays", target = "maxAdvanceBookingDays")
    @Mapping(source = "gapMinutes", target = "gapMinutes")
    @Mapping(source = "allowOvertime", target = "allowOvertime")
    AppointmentConfigResponse toResponse(AppointmentConfig entity);

    @Mapping(target = "id", ignore = true)
    void updateFromRequest(AppointmentConfigRequest request, @MappingTarget AppointmentConfig entity);

    @Mapping(target = "id", ignore = true)
    AppointmentConfig toEntity(AppointmentConfigRequest request);
}
