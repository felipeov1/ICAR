package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.CompanyCustomerRequest;
import com.icar.platform.api.dto.response.carwash.profile.CompanyCustomerResponse;
import com.icar.platform.domain.model.carwash.profile.CompanyCustomer;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T19:18:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class CompanyCustomerMapperImpl implements CompanyCustomerMapper {

    @Override
    public CompanyCustomerResponse toResponse(CompanyCustomer entity) {
        if ( entity == null ) {
            return null;
        }

        String name = null;
        String number = null;
        UUID id = null;
        String phone = null;
        String zipCode = null;
        String street = null;
        String neighborhood = null;
        String city = null;
        String state = null;
        String additionalInstructions = null;

        name = entity.getFullName();
        number = entity.getStreetNumber();
        id = entity.getId();
        phone = entity.getPhone();
        zipCode = entity.getZipCode();
        street = entity.getStreet();
        neighborhood = entity.getNeighborhood();
        city = entity.getCity();
        state = entity.getState();
        additionalInstructions = entity.getAdditionalInstructions();

        CompanyCustomerResponse companyCustomerResponse = new CompanyCustomerResponse( id, name, phone, zipCode, street, number, neighborhood, city, state, additionalInstructions );

        return companyCustomerResponse;
    }

    @Override
    public CompanyCustomer toEntity(CompanyCustomerRequest request) {
        if ( request == null ) {
            return null;
        }

        CompanyCustomer companyCustomer = new CompanyCustomer();

        companyCustomer.setFullName( request.fullName() );
        companyCustomer.setStreetNumber( request.streetNumber() );
        companyCustomer.setPhone( request.phone() );
        companyCustomer.setZipCode( request.zipCode() );
        companyCustomer.setStreet( request.street() );
        companyCustomer.setNeighborhood( request.neighborhood() );
        companyCustomer.setCity( request.city() );
        companyCustomer.setState( request.state() );
        companyCustomer.setAdditionalInstructions( request.additionalInstructions() );

        return companyCustomer;
    }
}
