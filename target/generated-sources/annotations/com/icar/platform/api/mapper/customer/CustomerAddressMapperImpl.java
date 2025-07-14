package com.icar.platform.api.mapper.customer;

import com.icar.platform.api.dto.request.customer.CustomerAddressRequest;
import com.icar.platform.api.dto.response.customer.CustomerAddressResponse;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.customer.CustomerAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.UUID;
import javax.annotation.processing.Generated;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-13T20:04:59-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class CustomerAddressMapperImpl implements CustomerAddressMapper {

    private final DatatypeFactory datatypeFactory;

    public CustomerAddressMapperImpl() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        }
        catch ( DatatypeConfigurationException ex ) {
            throw new RuntimeException( ex );
        }
    }

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
        createdAt = xmlGregorianCalendarToLocalDateTime( zonedDateTimeToXmlGregorianCalendar( entity.getCreatedAt() ) );
        updatedAt = xmlGregorianCalendarToLocalDateTime( zonedDateTimeToXmlGregorianCalendar( entity.getUpdatedAt() ) );

        CustomerAddressResponse customerAddressResponse = new CustomerAddressResponse( id, addressName, zipCode, city, state, street, neighborhood, streetNumber, additionalInstructions, createdAt, updatedAt );

        return customerAddressResponse;
    }

    private static LocalDateTime xmlGregorianCalendarToLocalDateTime( XMLGregorianCalendar xcal ) {
        if ( xcal == null ) {
            return null;
        }

        if ( xcal.getYear() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getMonth() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getDay() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getHour() != DatatypeConstants.FIELD_UNDEFINED
            && xcal.getMinute() != DatatypeConstants.FIELD_UNDEFINED
        ) {
            if ( xcal.getSecond() != DatatypeConstants.FIELD_UNDEFINED
                && xcal.getMillisecond() != DatatypeConstants.FIELD_UNDEFINED ) {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute(),
                    xcal.getSecond(),
                    Duration.ofMillis( xcal.getMillisecond() ).getNano()
                );
            }
            else if ( xcal.getSecond() != DatatypeConstants.FIELD_UNDEFINED ) {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute(),
                    xcal.getSecond()
                );
            }
            else {
                return LocalDateTime.of(
                    xcal.getYear(),
                    xcal.getMonth(),
                    xcal.getDay(),
                    xcal.getHour(),
                    xcal.getMinute()
                );
            }
        }
        return null;
    }

    private XMLGregorianCalendar zonedDateTimeToXmlGregorianCalendar( ZonedDateTime zdt ) {
        if ( zdt == null ) {
            return null;
        }

        return datatypeFactory.newXMLGregorianCalendar( GregorianCalendar.from( zdt ) );
    }
}
