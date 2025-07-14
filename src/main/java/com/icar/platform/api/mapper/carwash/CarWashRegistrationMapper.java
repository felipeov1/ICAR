package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.CarWashRegistrationRequest;
import com.icar.platform.api.dto.response.carwash.CarWashRegistrationResponse;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarWashRegistrationMapper {

    CarWashRegistration toEntity(CarWashRegistrationRequest dto);
    CarWashRegistrationResponse toDto(CarWashRegistration entity);
}