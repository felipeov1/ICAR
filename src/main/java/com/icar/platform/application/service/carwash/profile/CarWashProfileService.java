package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashProfileResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface CarWashProfileService {
    @Transactional
    CarWashProfileResponse createProfile( UUID carWashId,  CarWashProfileRequest request);

    @Transactional(readOnly = true)
    CarWashProfileResponse getProfileById( UUID profileId);

    @Transactional
    CarWashProfileResponse updateProfile(UUID profileId, CarWashProfileRequest request);

    @Transactional(readOnly = true)
    List<CarWashProfileResponse> findAllForMarketplace();

}