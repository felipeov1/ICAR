package com.icar.plataform.api.mapper.customer;

import com.icar.plataform.domain.model.customer.Customer;
import com.icar.plataform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.plataform.api.dto.response.auth.RegisterCustomerResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "status", expression = "java(com.icar.plataform.domain.enums.UserStatus.PENDING)") // Alterado para PENDING
    @Mapping(target = "password", ignore = true)
    Customer toEntity(RegisterCustomerRequest request);

    RegisterCustomerResponse toDto(Customer customer);
}
