package com.icar.plataform.application.service.carwash.offering;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashOfferingResponse;

import java.util.List;
import java.util.UUID;

public interface CarWashOfferingService {
    CarWashOfferingResponse create(UUID carWashProfileId, CarWashOfferingRequest request);
    CarWashOfferingResponse update(UUID offeringId, CarWashOfferingRequest request);
    void deactivate(UUID offeringId);
    List<CarWashOfferingResponse> findAllByCarWashProfile(UUID carWashProfileId);
}
