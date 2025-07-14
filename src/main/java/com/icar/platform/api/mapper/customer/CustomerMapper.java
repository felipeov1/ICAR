package com.icar.platform.api.mapper.customer;

import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.platform.api.dto.response.auth.RegisterCustomerResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "status", expression = "java(com.icar.platform.domain.enums.UserStatus.PENDING)")
    @Mapping(target = "password", ignore = true)
    Customer toEntity(RegisterCustomerRequest request);

    RegisterCustomerResponse toDto(Customer customer);
}
