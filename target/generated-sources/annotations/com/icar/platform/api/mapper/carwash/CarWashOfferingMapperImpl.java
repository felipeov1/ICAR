package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashOfferingResponse;
import com.icar.platform.domain.enums.DirtLevel;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import com.icar.platform.domain.model.carwash.offering.VehicleOfferingDetail;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-07T15:56:40-0300",
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

        carWashOffering.setDirtLevelRecommendation( dto.dirtLevelRecommendation() );
        carWashOffering.setName( dto.name() );
        carWashOffering.setDescription( dto.description() );
        carWashOffering.setServiceType( dto.serviceType() );
        carWashOffering.setModality( dto.modality() );
        carWashOffering.setVehicleDetails( stringVehicleDetailRequestMapToStringVehicleOfferingDetailMap( dto.vehicleDetails() ) );

        carWashOffering.setActive( true );

        return carWashOffering;
    }

    @Override
    public CarWashOfferingResponse toDto(CarWashOffering entity) {
        if ( entity == null ) {
            return null;
        }

        Map<String, CarWashOfferingResponse.VehicleDetailResponse> vehicleDetails = null;
        DirtLevel dirtLevelRecommendation = null;
        UUID id = null;
        String name = null;
        String description = null;
        String serviceType = null;
        String modality = null;
        boolean active = false;

        vehicleDetails = stringVehicleOfferingDetailMapToStringVehicleDetailResponseMap( entity.getVehicleDetails() );
        dirtLevelRecommendation = entity.getDirtLevelRecommendation();
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        serviceType = entity.getServiceType();
        modality = entity.getModality();
        active = entity.isActive();

        CarWashOfferingResponse carWashOfferingResponse = new CarWashOfferingResponse( id, name, description, serviceType, modality, active, dirtLevelRecommendation, vehicleDetails );

        return carWashOfferingResponse;
    }

    @Override
    public void updateEntity(CarWashOfferingRequest dto, CarWashOffering entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.dirtLevelRecommendation() != null ) {
            entity.setDirtLevelRecommendation( dto.dirtLevelRecommendation() );
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
        if ( dto.modality() != null ) {
            entity.setModality( dto.modality() );
        }
        if ( entity.getVehicleDetails() != null ) {
            Map<String, VehicleOfferingDetail> map = stringVehicleDetailRequestMapToStringVehicleOfferingDetailMap( dto.vehicleDetails() );
            if ( map != null ) {
                entity.getVehicleDetails().clear();
                entity.getVehicleDetails().putAll( map );
            }
        }
        else {
            Map<String, VehicleOfferingDetail> map = stringVehicleDetailRequestMapToStringVehicleOfferingDetailMap( dto.vehicleDetails() );
            if ( map != null ) {
                entity.setVehicleDetails( map );
            }
        }
    }

    @Override
    public VehicleOfferingDetail toVehicleDetailEntity(CarWashOfferingRequest.VehicleDetailRequest dto) {
        if ( dto == null ) {
            return null;
        }

        VehicleOfferingDetail vehicleOfferingDetail = new VehicleOfferingDetail();

        vehicleOfferingDetail.setPrice( dto.price() );
        vehicleOfferingDetail.setEstimatedTime( dto.durationMinutes() );

        return vehicleOfferingDetail;
    }

    @Override
    public CarWashOfferingResponse.VehicleDetailResponse toVehicleDetailDto(VehicleOfferingDetail entity) {
        if ( entity == null ) {
            return null;
        }

        BigDecimal price = null;
        Integer durationMinutes = null;

        price = entity.getPrice();
        durationMinutes = entity.getEstimatedTime();

        CarWashOfferingResponse.VehicleDetailResponse vehicleDetailResponse = new CarWashOfferingResponse.VehicleDetailResponse( price, durationMinutes );

        return vehicleDetailResponse;
    }

    protected Map<String, VehicleOfferingDetail> stringVehicleDetailRequestMapToStringVehicleOfferingDetailMap(Map<String, CarWashOfferingRequest.VehicleDetailRequest> map) {
        if ( map == null ) {
            return null;
        }

        Map<String, VehicleOfferingDetail> map1 = LinkedHashMap.newLinkedHashMap( map.size() );

        for ( java.util.Map.Entry<String, CarWashOfferingRequest.VehicleDetailRequest> entry : map.entrySet() ) {
            String key = entry.getKey();
            VehicleOfferingDetail value = toVehicleDetailEntity( entry.getValue() );
            map1.put( key, value );
        }

        return map1;
    }

    protected Map<String, CarWashOfferingResponse.VehicleDetailResponse> stringVehicleOfferingDetailMapToStringVehicleDetailResponseMap(Map<String, VehicleOfferingDetail> map) {
        if ( map == null ) {
            return null;
        }

        Map<String, CarWashOfferingResponse.VehicleDetailResponse> map1 = LinkedHashMap.newLinkedHashMap( map.size() );

        for ( java.util.Map.Entry<String, VehicleOfferingDetail> entry : map.entrySet() ) {
            String key = entry.getKey();
            CarWashOfferingResponse.VehicleDetailResponse value = toVehicleDetailDto( entry.getValue() );
            map1.put( key, value );
        }

        return map1;
    }
}
