package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.CarWashRegistrationRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashRegistrationResponse;
import com.icar.plataform.domain.model.carwash.legal.CarWashRegistration;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T19:41:33-0300",
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
        carWashRegistration.setAddress( dto.address() );
        carWashRegistration.setSubdomain( dto.subdomain() );

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
        String address = null;
        String subdomain = null;

        id = entity.getId();
        cnpj = entity.getCnpj();
        cpf = entity.getCpf();
        legalName = entity.getLegalName();
        tradeName = entity.getTradeName();
        ownerName = entity.getOwnerName();
        phone = entity.getPhone();
        email = entity.getEmail();
        address = entity.getAddress();
        subdomain = entity.getSubdomain();

        CarWashRegistrationResponse carWashRegistrationResponse = new CarWashRegistrationResponse( id, cnpj, cpf, legalName, tradeName, ownerName, phone, email, address, subdomain );

        return carWashRegistrationResponse;
    }
}
