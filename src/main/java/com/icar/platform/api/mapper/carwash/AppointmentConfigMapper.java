package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.AppointmentConfigRequest;
import com.icar.platform.api.dto.response.carwash.profile.AppointmentConfigResponse;
import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentConfigMapper {

    AppointmentConfigResponse toResponse(AppointmentConfig entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "minAdvanceNoticeMinutes", target = "minAdvanceNoticeMinutes")
    @Mapping(source = "maxAdvanceBookingDays", target = "maxAdvanceBookingDays")
    @Mapping(source = "minEditNoticeMinutes", target = "minEditNoticeMinutes")
    @Mapping(source = "minCancelNoticeMinutes", target = "minCancelNoticeMinutes")
    @Mapping(source = "gapMinutes", target = "gapMinutes")
    @Mapping(source = "allowOvertime", target = "allowOvertime")
    void updateFromRequest(AppointmentConfigRequest request, @MappingTarget AppointmentConfig entity);

    @Mapping(target = "id", ignore = true)
    AppointmentConfig toEntity(AppointmentConfigRequest request);
}