package com.icar.platform.application.service.carwash.legal;

import com.icar.platform.api.dto.request.carwash.CarWashRegistrationRequest;
import com.icar.platform.api.dto.response.carwash.CarWashRegistrationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface CarWashRegistrationService {
    CarWashRegistrationResponse create(CarWashRegistrationRequest dto);
    List<CarWashRegistrationResponse> findAllByOrderByCreatedAtAsc();
    CarWashRegistrationResponse findById(UUID id);
    CarWashRegistrationResponse findBySubdomain(String slug);
    CarWashRegistrationResponse update(UUID id, CarWashRegistrationRequest dto);
    void deactivate(UUID id);
}