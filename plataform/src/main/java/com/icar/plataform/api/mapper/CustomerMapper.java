package com.icar.plataform.api.mapper;

import com.icar.plataform.domain.model.Customer;
import com.icar.plataform.api.dto.request.CustomerCreateRequest;
import com.icar.plataform.api.dto.response.CustomerCreateResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "status", expression = "java(com.icar.plataform.domain.enums.UserStatus.PENDING)") // Alterado para PENDING
    @Mapping(target = "password", ignore = true)
    Customer toEntity(CustomerCreateRequest request);

    CustomerCreateResponse toDto(Customer customer);
}
