package com.icar.plataform.application.service.carwash;

import com.icar.plataform.api.dto.request.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.CarWashOfferingResponse;

import java.util.List;
import java.util.UUID;

public interface CarWashOfferingService {
    CarWashOfferingResponse create(UUID carWashId, CarWashOfferingRequest request);
    CarWashOfferingResponse update(UUID serviceId, CarWashOfferingRequest request);
    void deactivate(UUID serviceId);
    List<CarWashOfferingResponse> findAllByCarWash(UUID carWashId);
}
