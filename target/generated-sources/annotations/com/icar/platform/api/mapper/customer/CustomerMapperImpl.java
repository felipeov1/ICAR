package com.icar.platform.api.mapper.customer;

import com.icar.platform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.platform.api.dto.response.auth.RegisterCustomerResponse;
import com.icar.platform.domain.model.customer.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-13T13:52:24-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(RegisterCustomerRequest request) {
        if ( request == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.fullName( request.fullName() );
        customer.email( request.email() );
        customer.phone( request.phone() );

        customer.status( com.icar.platform.domain.enums.UserStatus.PENDING );

        return customer.build();
    }

    @Override
    public RegisterCustomerResponse toDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        RegisterCustomerResponse.RegisterCustomerResponseBuilder registerCustomerResponse = RegisterCustomerResponse.builder();

        registerCustomerResponse.id( customer.getId() );
        registerCustomerResponse.fullName( customer.getFullName() );
        registerCustomerResponse.email( customer.getEmail() );
        registerCustomerResponse.phone( customer.getPhone() );
        registerCustomerResponse.status( customer.getStatus() );

        return registerCustomerResponse.build();
    }
}
