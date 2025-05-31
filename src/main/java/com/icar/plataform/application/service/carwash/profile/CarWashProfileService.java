package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashProfileResponse;

import java.util.UUID;

public interface CarWashProfileService {
    CarWashProfileResponse createProfile(UUID carWashId, CarWashProfileRequest request);
    CarWashProfileResponse getProfileByCarWashId(UUID carWashId);
    CarWashProfileResponse updateProfile(UUID carWashId, CarWashProfileRequest request);
}