package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.CarWashRegistrationRequest;
import com.icar.platform.api.dto.response.carwash.CarWashRegistrationResponse;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-04T20:30:57-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
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
        carWashRegistration.setTradeName( dto.tradeName() );
        carWashRegistration.setOwnerName( dto.ownerName() );
        carWashRegistration.setPhone( dto.phone() );
        carWashRegistration.setEmail( dto.email() );
        carWashRegistration.setStreet( dto.street() );
        carWashRegistration.setNumber( dto.number() );
        carWashRegistration.setComplement( dto.complement() );
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
        String cpf = null;
        String legalName = null;
        String tradeName = null;
        String ownerName = null;
        String phone = null;
        String email = null;
        String street = null;
        String number = null;
        String complement = null;
        String neighborhood = null;
        String city = null;
        String state = null;
        String zipCode = null;

        id = entity.getId();
        cnpj = entity.getCnpj();
        cpf = entity.getCpf();
        legalName = entity.getLegalName();
        tradeName = entity.getTradeName();
        ownerName = entity.getOwnerName();
        phone = entity.getPhone();
        email = entity.getEmail();
        street = entity.getStreet();
        number = entity.getNumber();
        complement = entity.getComplement();
        neighborhood = entity.getNeighborhood();
        city = entity.getCity();
        state = entity.getState();
        zipCode = entity.getZipCode();

        CarWashRegistrationResponse carWashRegistrationResponse = new CarWashRegistrationResponse( id, cnpj, cpf, legalName, tradeName, ownerName, phone, email, street, number, complement, neighborhood, city, state, zipCode );

        return carWashRegistrationResponse;
    }
}
