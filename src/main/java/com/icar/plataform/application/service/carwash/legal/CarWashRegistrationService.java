package com.icar.plataform.application.service.carwash.legal;

import com.icar.plataform.api.dto.request.carwash.CarWashRegistrationRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashRegistrationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface CarWashRegistrationService {
    CarWashRegistrationResponse create(CarWashRegistrationRequest dto);
    List<CarWashRegistrationResponse> findAllByOrderByCreatedAtAsc();
    CarWashRegistrationResponse findById(UUID id);
}