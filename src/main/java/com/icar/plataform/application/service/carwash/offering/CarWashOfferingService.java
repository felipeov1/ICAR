package com.icar.plataform.application.service.carwash.offering;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashOfferingResponse;

import java.util.List;
import java.util.UUID;

public interface CarWashOfferingService {
    CarWashOfferingResponse create(UUID carWashId, CarWashOfferingRequest request);
    List<CarWashOfferingResponse> findAllByCarWashId(UUID carWashId);
    CarWashOfferingResponse update(UUID offeringId, CarWashOfferingRequest request);
    void deactivate(UUID offeringId);
    void activate(UUID offeringId);
}