package com.icar.plataform.mapper;

import com.icar.plataform.domain.model.Service;
import com.icar.plataform.dto.response.ServiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "modality", source = "modality")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "estimatedTime", source = "estimatedTime")
    ServiceResponse toDto(Service entity);
}