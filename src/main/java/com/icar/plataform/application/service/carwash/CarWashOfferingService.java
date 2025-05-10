package com.icar.plataform.application.service.carwash;

import com.icar.plataform.api.dto.request.carwash.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashOfferingResponse;

import java.util.List;
import java.util.UUID;

public interface CarWashOfferingService {
    CarWashOfferingResponse create(UUID carWashProfileId, CarWashOfferingRequest request);
    CarWashOfferingResponse update(UUID offeringId, CarWashOfferingRequest request);
    void deactivate(UUID offeringId);
    List<CarWashOfferingResponse> findAllByCarWashProfile(UUID carWashProfileId);
}
