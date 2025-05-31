package com.icar.plataform.api.mapper.customer;

import com.icar.plataform.api.dto.request.customer.CustomerAddressRequest;
import com.icar.plataform.api.dto.response.customer.CustomerAddressResponse;
import com.icar.plataform.domain.model.customer.Customer;
import com.icar.plataform.domain.model.customer.CustomerAddress;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerAddressMapper {

    @Mapping(target = "streetNumber", source = "streetNumber")
    CustomerAddress toEntity(CustomerAddressRequest request, @Context Customer customer);

    @Mapping(target = "streetNumber", source = "streetNumber")  
    CustomerAddressResponse toResponse(CustomerAddress entity);

    @AfterMapping
    default void setCustomer(@MappingTarget CustomerAddress address, @Context Customer customer) {
        address.setCustomer(customer);
    }
}
