package com.icar.plataform.application.service.carwash;

import com.icar.plataform.api.dto.request.CarWashRequest;
import com.icar.plataform.api.dto.response.CarWashResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface CarWashService {
    CarWashResponse create(CarWashRequest dto);
    List<CarWashResponse> findAllByOrderByCreatedAtAsc();
    CarWashResponse findById(UUID id);
}