package com.icar.platform.api.mapper.customer;

import com.icar.platform.api.dto.request.customer.CustomerAddressRequest;
import com.icar.platform.api.dto.response.customer.CustomerAddressResponse;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.customer.CustomerAddress;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-15T11:02:35-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class CustomerAddressMapperImpl implements CustomerAddressMapper {

    @Override
    public CustomerAddress toEntity(CustomerAddressRequest request, Customer customer) {
        if ( request == null ) {
            return null;
        }

        CustomerAddress.CustomerAddressBuilder customerAddress = CustomerAddress.builder();

        customerAddress.streetNumber( request.streetNumber() );
        customerAddress.addressName( request.addressName() );
        customerAddress.zipCode( request.zipCode() );
        customerAddress.city( request.city() );
        customerAddress.state( request.state() );
        customerAddress.street( request.street() );
        customerAddress.neighborhood( request.neighborhood() );
        customerAddress.additionalInstructions( request.additionalInstructions() );

        CustomerAddress customerAddressResult = customerAddress.build();

        setCustomer( customerAddressResult, customer );

        return customerAddressResult;
    }

    @Override
    public CustomerAddressResponse toResponse(CustomerAddress entity) {
        if ( entity == null ) {
            return null;
        }

        String streetNumber = null;
        UUID id = null;
        String addressName = null;
        String zipCode = null;
        String city = null;
        String state = null;
        String street = null;
        String neighborhood = null;
        String additionalInstructions = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        streetNumber = entity.getStreetNumber();
        id = entity.getId();
        addressName = entity.getAddressName();
        zipCode = entity.getZipCode();
        city = entity.getCity();
        state = entity.getState();
        street = entity.getStreet();
        neighborhood = entity.getNeighborhood();
        additionalInstructions = entity.getAdditionalInstructions();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        CustomerAddressResponse customerAddressResponse = new CustomerAddressResponse( id, addressName, zipCode, city, state, street, neighborhood, streetNumber, additionalInstructions, createdAt, updatedAt );

        return customerAddressResponse;
    }
}
