package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileUpdateRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-27T22:47:25+0000",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class CarWashProfileMapperImpl implements CarWashProfileMapper {

    @Override
    public CarWashProfile toEntity(CarWashProfileRequest request) {
        if ( request == null ) {
            return null;
        }

        CarWashProfile carWashProfile = new CarWashProfile();

        carWashProfile.setName( request.getName() );
        carWashProfile.setDescription( request.getDescription() );
        String[] modalities = request.getModalities();
        if ( modalities != null ) {
            carWashProfile.setModalities( Arrays.copyOf( modalities, modalities.length ) );
        }
        carWashProfile.setSubdomain( request.getSubdomain() );
        carWashProfile.setWhatsapp( request.getWhatsapp() );
        String[] locations = request.getLocations();
        if ( locations != null ) {
            carWashProfile.setLocations( Arrays.copyOf( locations, locations.length ) );
        }
        carWashProfile.setObservations( request.getObservations() );

        return carWashProfile;
    }

    @Override
    public void updateEntity(CarWashProfileUpdateRequest request, CarWashProfile entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            entity.setName( request.getName() );
        }
        if ( request.getDescription() != null ) {
            entity.setDescription( request.getDescription() );
        }
        if ( request.getModalities() != null ) {
            entity.setModalities( stringListToStringArray( request.getModalities() ) );
        }
        if ( request.getWhatsapp() != null ) {
            entity.setWhatsapp( request.getWhatsapp() );
        }
        if ( request.getLocations() != null ) {
            entity.setLocations( stringListToStringArray( request.getLocations() ) );
        }
        if ( request.getObservations() != null ) {
            entity.setObservations( request.getObservations() );
        }
    }

    @Override
    public CarWashProfileResponse toDto(CarWashProfile carWashProfile) {
        if ( carWashProfile == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;
        String logo = null;
        BigDecimal rating = null;
        Integer reviews = null;
        String[] modalities = null;
        String coverPhoto = null;
        String subdomain = null;
        String whatsapp = null;
        String[] locations = null;
        String observations = null;

        id = carWashProfile.getId();
        name = carWashProfile.getName();
        description = carWashProfile.getDescription();
        logo = carWashProfile.getLogo();
        rating = carWashProfile.getRating();
        reviews = carWashProfile.getReviews();
        String[] modalities1 = carWashProfile.getModalities();
        if ( modalities1 != null ) {
            modalities = Arrays.copyOf( modalities1, modalities1.length );
        }
        coverPhoto = carWashProfile.getCoverPhoto();
        subdomain = carWashProfile.getSubdomain();
        whatsapp = carWashProfile.getWhatsapp();
        String[] locations1 = carWashProfile.getLocations();
        if ( locations1 != null ) {
            locations = Arrays.copyOf( locations1, locations1.length );
        }
        observations = carWashProfile.getObservations();

        boolean mercadoPagoConnected = carWashProfile.getCarWashRegistration() != null && carWashProfile.getCarWashRegistration().getMercadoPagoConfig() != null;

        CarWashProfileResponse carWashProfileResponse = new CarWashProfileResponse( id, name, description, logo, rating, reviews, modalities, coverPhoto, subdomain, whatsapp, locations, observations, mercadoPagoConnected );

        return carWashProfileResponse;
    }

    @Override
    public void updateEntity(CarWashProfileRequest request, CarWashProfile entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            entity.setName( request.getName() );
        }
        if ( request.getDescription() != null ) {
            entity.setDescription( request.getDescription() );
        }
        String[] modalities = request.getModalities();
        if ( modalities != null ) {
            entity.setModalities( Arrays.copyOf( modalities, modalities.length ) );
        }
        if ( request.getSubdomain() != null ) {
            entity.setSubdomain( request.getSubdomain() );
        }
        if ( request.getWhatsapp() != null ) {
            entity.setWhatsapp( request.getWhatsapp() );
        }
        String[] locations = request.getLocations();
        if ( locations != null ) {
            entity.setLocations( Arrays.copyOf( locations, locations.length ) );
        }
        if ( request.getObservations() != null ) {
            entity.setObservations( request.getObservations() );
        }
    }

    protected String[] stringListToStringArray(List<String> list) {
        if ( list == null ) {
            return null;
        }

        String[] stringTmp = new String[list.size()];
        int i = 0;
        for ( String string : list ) {
            stringTmp[i] = string;
            i++;
        }

        return stringTmp;
    }
}
