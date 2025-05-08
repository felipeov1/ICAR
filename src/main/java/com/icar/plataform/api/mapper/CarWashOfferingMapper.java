package com.icar.plataform.api.mapper;

import com.icar.plataform.api.dto.request.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.CarWashOfferingResponse;
import com.icar.plataform.domain.model.CarWashOffering;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarWashOfferingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carWash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CarWashOffering toEntity(CarWashOfferingRequest dto);

    CarWashOfferingResponse toDto(CarWashOffering entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carWash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(CarWashOfferingRequest dto, @MappingTarget CarWashOffering entity);
}
