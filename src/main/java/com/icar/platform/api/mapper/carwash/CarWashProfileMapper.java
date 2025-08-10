package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileUpdateRequest;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarWashProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carWashRegistration", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "offerings", ignore = true)
    @Mapping(target = "weeklySchedules", ignore = true)
    @Mapping(target = "specialDays", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "coverPhoto", ignore = true)
    @Mapping(target = "photos", ignore = true)
    CarWashProfile toEntity(CarWashProfileRequest request);
    void updateEntity(CarWashProfileUpdateRequest request, @MappingTarget CarWashProfile entity);

    CarWashProfileResponse toDto(CarWashProfile carWashProfile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carWashRegistration", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "offerings", ignore = true)
    @Mapping(target = "weeklySchedules", ignore = true)
    @Mapping(target = "specialDays", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "coverPhoto", ignore = true)
    @Mapping(target = "photos", ignore = true)
    void updateEntity(CarWashProfileRequest request, @MappingTarget CarWashProfile entity);
}