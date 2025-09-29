package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashOfferingResponse;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import com.icar.platform.domain.model.carwash.offering.VehicleOfferingDetail;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarWashOfferingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "dirtLevelRecommendation", source = "dirtLevelRecommendation")
    CarWashOffering toEntity(CarWashOfferingRequest dto);

    @Mapping(target = "vehicleDetails", source = "vehicleDetails")
    @Mapping(target = "dirtLevelRecommendation", source = "dirtLevelRecommendation")
    CarWashOfferingResponse toDto(CarWashOffering entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "dirtLevelRecommendation", source = "dirtLevelRecommendation")
    void updateEntity(CarWashOfferingRequest dto, @MappingTarget CarWashOffering entity);

    @Mapping(source = "price", target = "price")
    @Mapping(source = "durationMinutes", target = "estimatedTime")
    VehicleOfferingDetail toVehicleDetailEntity(CarWashOfferingRequest.VehicleDetailRequest dto);

    @Mapping(source = "price", target = "price")
    @Mapping(source = "estimatedTime", target = "durationMinutes")
    CarWashOfferingResponse.VehicleDetailResponse toVehicleDetailDto(VehicleOfferingDetail entity);
}