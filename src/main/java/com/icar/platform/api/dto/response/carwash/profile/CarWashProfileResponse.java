package com.icar.platform.api.dto.response.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import java.math.BigDecimal;
import java.util.UUID;

public record CarWashProfileResponse(
        UUID id,
        String name,
        String description,
        String logo,
        BigDecimal rating,
        Integer reviews,
        String[] modalities,
        String coverPhoto,
        String subdomain,
        String whatsapp,
        String[] locations,
        String observations
) {

    public static CarWashProfileResponse fromProfile(CarWashProfile profile) {
        return new CarWashProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getDescription(),
                profile.getLogo(),
                profile.getRating(),
                profile.getReviews(),
                profile.getModalities(),
                profile.getCoverPhoto(),
                profile.getSubdomain(),
                profile.getWhatsapp(),
                profile.getLocations(),
                profile.getObservations()
        );
    }
}