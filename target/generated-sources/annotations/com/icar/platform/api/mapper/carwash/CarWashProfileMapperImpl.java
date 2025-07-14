package com.icar.platform.api.mapper.carwash;

import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-13T20:04:59-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class CarWashProfileMapperImpl implements CarWashProfileMapper {

    @Override
    public CarWashProfile toEntity(CarWashProfileRequest carWashProfileRequest) {
        if ( carWashProfileRequest == null ) {
            return null;
        }

        CarWashProfile carWashProfile = new CarWashProfile();

        carWashProfile.setName( carWashProfileRequest.getName() );
        carWashProfile.setDescription( carWashProfileRequest.getDescription() );
        carWashProfile.setPriceRange( carWashProfileRequest.getPriceRange() );
        String[] modalities = carWashProfileRequest.getModalities();
        if ( modalities != null ) {
            carWashProfile.setModalities( Arrays.copyOf( modalities, modalities.length ) );
        }
        carWashProfile.setLogo( carWashProfileRequest.getLogo() );
        carWashProfile.setCoverPhoto( carWashProfileRequest.getCoverPhoto() );

        return carWashProfile;
    }

    @Override
    public CarWashProfileResponse toDto(CarWashProfile carWashProfile) {
        if ( carWashProfile == null ) {
            return null;
        }

        String logo = null;
        UUID id = null;
        String name = null;
        String description = null;
        BigDecimal rating = null;
        Integer reviews = null;
        String[] modalities = null;
        String priceRange = null;
        String coverPhoto = null;

        logo = carWashProfile.getLogo();
        id = carWashProfile.getId();
        name = carWashProfile.getName();
        description = carWashProfile.getDescription();
        rating = carWashProfile.getRating();
        reviews = carWashProfile.getReviews();
        String[] modalities1 = carWashProfile.getModalities();
        if ( modalities1 != null ) {
            modalities = Arrays.copyOf( modalities1, modalities1.length );
        }
        priceRange = carWashProfile.getPriceRange();
        coverPhoto = carWashProfile.getCoverPhoto();

        CarWashProfileResponse carWashProfileResponse = new CarWashProfileResponse( id, name, description, logo, rating, reviews, modalities, priceRange, coverPhoto );

        return carWashProfileResponse;
    }

    @Override
    public void updateEntity(CarWashProfileRequest request, CarWashProfile entity) {
        if ( request == null ) {
            return;
        }

        entity.setName( request.getName() );
        entity.setDescription( request.getDescription() );
        entity.setPriceRange( request.getPriceRange() );
        String[] modalities = request.getModalities();
        if ( modalities != null ) {
            entity.setModalities( Arrays.copyOf( modalities, modalities.length ) );
        }
        else {
            entity.setModalities( null );
        }
        entity.setLogo( request.getLogo() );
        entity.setCoverPhoto( request.getCoverPhoto() );
    }
}
