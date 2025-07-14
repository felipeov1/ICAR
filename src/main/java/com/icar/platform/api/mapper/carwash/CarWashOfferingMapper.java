package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashOfferingResponse;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarWashOfferingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "serviceType", source = "serviceType")
    CarWashOffering toEntity(CarWashOfferingRequest dto);

    @Mapping(target = "statusText", expression = "java(getStatusText(entity))")
    @Mapping(target = "serviceType", source = "serviceType")
    @Mapping(target = "vehiclePrices", source = "vehiclePrices")
    @Mapping(target = "vehicleEstimatedTimes", source = "vehicleEstimatedTimes")
    CarWashOfferingResponse toDto(CarWashOffering entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(CarWashOfferingRequest dto, @MappingTarget CarWashOffering entity);


    default String getStatusText(CarWashOffering entity) {
        if (entity.getDeletedAt() != null) return "Excluído";
        return entity.isActive() ? "Ativo" : "Inativo";
    }
}