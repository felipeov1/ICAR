package com.icar.platform.api.mapper.admin;

import com.icar.platform.api.dto.response.admin.PartnerResponse;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import java.time.LocalDate;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-12T19:15:13-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class PartnerMapperImpl implements PartnerMapper {

    @Override
    public PartnerResponse toPartnerResponse(CarWashRegistration partner) {
        if ( partner == null ) {
            return null;
        }

        LocalDate joinDate = null;
        UUID id = null;
        String tradeName = null;
        String ownerName = null;
        String email = null;
        String phone = null;
        String cnpj = null;
        String zipCode = null;
        String city = null;
        String state = null;

        joinDate = toLocalDate( partner.getCreatedAt() );
        id = partner.getId();
        tradeName = partner.getTradeName();
        ownerName = partner.getOwnerName();
        email = partner.getEmail();
        phone = partner.getPhone();
        cnpj = partner.getCnpj();
        zipCode = partner.getZipCode();
        city = partner.getCity();
        state = partner.getState();

        String status = calculatePartnerStatus(partner);
        PartnerResponse.CurrentPlanDto currentPlan = mapCurrentPlan(partner);

        PartnerResponse partnerResponse = new PartnerResponse( id, tradeName, ownerName, email, phone, cnpj, joinDate, status, currentPlan, zipCode, city, state );

        return partnerResponse;
    }
}
