package com.icar.plataform.api.mapper;

import com.icar.plataform.domain.model.Customer;
import com.icar.plataform.api.dto.request.RegisterCustomerRequest;
import com.icar.plataform.api.dto.response.RegisterCustomerResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "status", expression = "java(com.icar.plataform.domain.enums.UserStatus.PENDING)") // Alterado para PENDING
    @Mapping(target = "password", ignore = true)
    Customer toEntity(RegisterCustomerRequest request);

    RegisterCustomerResponse toDto(Customer customer);
}
