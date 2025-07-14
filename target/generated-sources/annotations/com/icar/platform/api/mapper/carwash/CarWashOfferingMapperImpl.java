package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashOfferingResponse;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-14T19:39:16-0300",
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

        carWashOffering.setServiceType( dto.serviceType() );
        carWashOffering.setName( dto.name() );
        carWashOffering.setDescription( dto.description() );
        if ( dto.active() != null ) {
            carWashOffering.setActive( dto.active() );
        }
        Map<String, BigDecimal> map = dto.vehiclePrices();
        if ( map != null ) {
            carWashOffering.setVehiclePrices( new LinkedHashMap<String, BigDecimal>( map ) );
        }
        Map<String, Integer> map1 = dto.vehicleEstimatedTimes();
        if ( map1 != null ) {
            carWashOffering.setVehicleEstimatedTimes( new LinkedHashMap<String, Integer>( map1 ) );
        }

        return carWashOffering;
    }

    @Override
    public CarWashOfferingResponse toDto(CarWashOffering entity) {
        if ( entity == null ) {
            return null;
        }

        String serviceType = null;
        Map<String, BigDecimal> vehiclePrices = null;
        Map<String, Integer> vehicleEstimatedTimes = null;
        UUID id = null;
        String name = null;
        String description = null;
        Boolean active = null;

        serviceType = entity.getServiceType();
        Map<String, BigDecimal> map = entity.getVehiclePrices();
        if ( map != null ) {
            vehiclePrices = new LinkedHashMap<String, BigDecimal>( map );
        }
        Map<String, Integer> map1 = entity.getVehicleEstimatedTimes();
        if ( map1 != null ) {
            vehicleEstimatedTimes = new LinkedHashMap<String, Integer>( map1 );
        }
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        active = entity.isActive();

        String statusText = getStatusText(entity);
        BigDecimal price = null;
        Integer estimatedTime = null;

        CarWashOfferingResponse carWashOfferingResponse = new CarWashOfferingResponse( id, name, description, price, estimatedTime, active, statusText, serviceType, vehiclePrices, vehicleEstimatedTimes );

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
        if ( dto.serviceType() != null ) {
            entity.setServiceType( dto.serviceType() );
        }
        if ( dto.active() != null ) {
            entity.setActive( dto.active() );
        }
        if ( entity.getVehiclePrices() != null ) {
            Map<String, BigDecimal> map = dto.vehiclePrices();
            if ( map != null ) {
                entity.getVehiclePrices().clear();
                entity.getVehiclePrices().putAll( map );
            }
        }
        else {
            Map<String, BigDecimal> map = dto.vehiclePrices();
            if ( map != null ) {
                entity.setVehiclePrices( new LinkedHashMap<String, BigDecimal>( map ) );
            }
        }
        if ( entity.getVehicleEstimatedTimes() != null ) {
            Map<String, Integer> map1 = dto.vehicleEstimatedTimes();
            if ( map1 != null ) {
                entity.getVehicleEstimatedTimes().clear();
                entity.getVehicleEstimatedTimes().putAll( map1 );
            }
        }
        else {
            Map<String, Integer> map1 = dto.vehicleEstimatedTimes();
            if ( map1 != null ) {
                entity.setVehicleEstimatedTimes( new LinkedHashMap<String, Integer>( map1 ) );
            }
        }
    }
}
