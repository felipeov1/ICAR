package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.plataform.api.dto.request.carwash.profile.CarWashScheduleConfigRequest;
import com.icar.plataform.api.dto.request.carwash.profile.ProfilePhotoRequest;
import com.icar.plataform.api.dto.request.carwash.profile.VehicleTypeRequest;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashProfileResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CarWashProfileService {
    CarWashProfileResponse createProfile(UUID carWashId, CarWashProfileRequest request);
    CarWashProfileResponse getProfileByCarWashId(UUID carWashId);
    CarWashProfileResponse updateProfile(UUID carWashId, CarWashProfileRequest request);
}