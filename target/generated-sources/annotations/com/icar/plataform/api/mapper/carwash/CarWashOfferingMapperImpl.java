package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashOfferingResponse;
import com.icar.plataform.domain.model.carwash.offering.CarWashOffering;
import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T19:41:33-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class CarWashOfferingMapperImpl implements CarWashOfferingMapper {

    @Override
    public CarWashOffering toEntity(CarWashOfferingRequest dto) {
        if ( dto == null ) {
            return null;
        }

        CarWashOffering carWashOffering = new CarWashOffering();

        carWashOffering.setEstimatedTime( dto.estimatedTime() );
        carWashOffering.setName( dto.name() );
        carWashOffering.setDescription( dto.description() );
        carWashOffering.setPrice( dto.price() );
        if ( dto.active() != null ) {
            carWashOffering.setActive( dto.active() );
        }

        return carWashOffering;
    }

    @Override
    public CarWashOfferingResponse toDto(CarWashOffering entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;
        BigDecimal price = null;
        Integer estimatedTime = null;
        Boolean active = null;

        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        estimatedTime = entity.getEstimatedTime();
        active = entity.isActive();

        String statusText = getStatusText(entity.isActive(), entity.getDeletedAt());
        String formattedPrice = formatPrice(entity.getPrice());
        String formattedTime = formatEstimatedTime(entity.getEstimatedTime());
        String modalityText = null;

        CarWashOfferingResponse carWashOfferingResponse = new CarWashOfferingResponse( id, name, description, price, formattedPrice, formattedTime, estimatedTime, modalityText, active, statusText );

        return carWashOfferingResponse;
    }

    @Override
    public void updateEntity(CarWashOfferingRequest dto, CarWashOffering entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.name() != null ) {
            entity.setName( dto.name() );
        }
        if ( dto.description() != null ) {
            entity.setDescription( dto.description() );
        }
        if ( dto.estimatedTime() != null ) {
            entity.setEstimatedTime( dto.estimatedTime() );
        }
        if ( dto.price() != null ) {
            entity.setPrice( dto.price() );
        }
        if ( dto.active() != null ) {
            entity.setActive( dto.active() );
        }
    }
}
