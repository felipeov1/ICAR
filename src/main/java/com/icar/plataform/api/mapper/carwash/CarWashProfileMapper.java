package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.domain.model.carwash.CarWashProfile;
import com.icar.plataform.api.dto.response.carwash.CarWashProfileResponse;
import com.icar.plataform.api.dto.request.carwash.CarWashProfileRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarWashProfileMapper {

    CarWashProfileResponse toDto(CarWashProfile carWashProfile);

    CarWashProfile toEntity(CarWashProfileRequest carWashProfileRequest);
}
