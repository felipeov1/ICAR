package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.CarWashRegistrationRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashRegistrationResponse;
import com.icar.plataform.domain.model.carwash.CarWashRegistration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarWashRegistrationMapper {

    CarWashRegistration toEntity(CarWashRegistrationRequest dto);
    CarWashRegistrationResponse toDto(CarWashRegistration entity);
}