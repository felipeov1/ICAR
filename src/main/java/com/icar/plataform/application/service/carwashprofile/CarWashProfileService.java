package com.icar.plataform.application.service.carwashprofile;

import com.icar.plataform.api.dto.request.carwash.CarWashProfileRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashProfileResponse;
import java.util.UUID;

public interface CarWashProfileService {

    CarWashProfileResponse createProfile(UUID carWashId, CarWashProfileRequest request);

    CarWashProfileResponse getProfileByCarWashId(UUID carWashId);
}
