package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.CarWashRegistrationRequest;
import com.icar.platform.api.dto.response.carwash.CarWashRegistrationResponse;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-27T17:43:59+0000",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class CarWashRegistrationMapperImpl implements CarWashRegistrationMapper {

    @Override
    public CarWashRegistration toEntity(CarWashRegistrationRequest dto) {
        if ( dto == null ) {
            return null;
        }

        CarWashRegistration carWashRegistration = new CarWashRegistration();

        carWashRegistration.setCnpj( dto.cnpj() );
        carWashRegistration.setCpf( dto.cpf() );
        carWashRegistration.setLegalName( dto.legalName() );
        carWashRegistration.setOwnerName( dto.ownerName() );
        carWashRegistration.setPhone( dto.phone() );
        carWashRegistration.setEmail( dto.email() );
        carWashRegistration.setStreet( dto.street() );
        carWashRegistration.setNumber( dto.number() );
        carWashRegistration.setNeighborhood( dto.neighborhood() );
        carWashRegistration.setCity( dto.city() );
        carWashRegistration.setState( dto.state() );
        carWashRegistration.setZipCode( dto.zipCode() );
        carWashRegistration.setPassword( dto.password() );

        return carWashRegistration;
    }

    @Override
    public CarWashRegistrationResponse toDto(CarWashRegistration entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        String cnpj = null;
        String legalName = null;
        String ownerName = null;
        String phone = null;
        String email = null;
        String street = null;
        String number = null;
        String neighborhood = null;
        String city = null;
        String state = null;
        String zipCode = null;

        id = entity.getId();
        cnpj = entity.getCnpj();
        legalName = entity.getLegalName();
        ownerName = entity.getOwnerName();
        phone = entity.getPhone();
        email = entity.getEmail();
        street = entity.getStreet();
        number = entity.getNumber();
        neighborhood = entity.getNeighborhood();
        city = entity.getCity();
        state = entity.getState();
        zipCode = entity.getZipCode();

        CarWashRegistrationResponse carWashRegistrationResponse = new CarWashRegistrationResponse( id, cnpj, legalName, ownerName, phone, email, street, number, neighborhood, city, state, zipCode );

        return carWashRegistrationResponse;
    }
}
