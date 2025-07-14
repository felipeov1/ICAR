package com.icar.platform.application.service.carwash.offering;

import com.icar.platform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashOfferingResponse;

import java.util.List;
import java.util.UUID;

public interface CarWashOfferingService {
    CarWashOfferingResponse create(UUID profileId, CarWashOfferingRequest request);
    List<CarWashOfferingResponse> findAllByProfileId(UUID profileId);
    List<CarWashOfferingResponse> findByProfileIdAndVehicleType(UUID profileId, String vehicleType);
    CarWashOfferingResponse update(UUID offeringId, CarWashOfferingRequest request);
    void deactivate(UUID offeringId);
    void activate(UUID offeringId);
}