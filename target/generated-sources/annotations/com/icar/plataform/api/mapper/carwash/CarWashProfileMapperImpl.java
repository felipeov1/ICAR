package com.icar.plataform.api.mapper.carwash;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T19:41:33-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class CarWashProfileMapperImpl implements CarWashProfileMapper {

    @Override
    public CarWashProfileResponse toDto(CarWashProfile carWashProfile) {
        if ( carWashProfile == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;
        String coverPhoto = null;

        id = carWashProfile.getId();
        name = carWashProfile.getName();
        description = carWashProfile.getDescription();
        coverPhoto = carWashProfile.getCoverPhoto();

        CarWashProfileResponse carWashProfileResponse = new CarWashProfileResponse( id, name, description, coverPhoto );

        return carWashProfileResponse;
    }

    @Override
    public CarWashProfile toEntity(CarWashProfileRequest carWashProfileRequest) {
        if ( carWashProfileRequest == null ) {
            return null;
        }

        CarWashProfile carWashProfile = new CarWashProfile();

        carWashProfile.setName( carWashProfileRequest.getName() );
        carWashProfile.setDescription( carWashProfileRequest.getDescription() );
        carWashProfile.setCoverPhoto( carWashProfileRequest.getCoverPhoto() );

        return carWashProfile;
    }

    @Override
    public void updateEntity(CarWashProfileRequest request, CarWashProfile entity) {
        if ( request == null ) {
            return;
        }

        entity.setName( request.getName() );
        entity.setDescription( request.getDescription() );
        entity.setCoverPhoto( request.getCoverPhoto() );
    }
}
