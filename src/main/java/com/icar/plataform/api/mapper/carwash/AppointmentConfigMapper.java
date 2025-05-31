package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.profile.AppointmentConfigRequest;
import com.icar.plataform.api.dto.response.carwash.profile.AppointmentConfigResponse;
import com.icar.plataform.domain.model.carwash.profile.AppointmentConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentConfigMapper {
    AppointmentConfigResponse toResponse(AppointmentConfig entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    void updateFromRequest(AppointmentConfigRequest request, @MappingTarget AppointmentConfig entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    AppointmentConfig toEntity(AppointmentConfigRequest request);
}