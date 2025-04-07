package com.icar.plataform.application.service.carwash;

import com.icar.plataform.api.dto.request.CarWashRequest;
import com.icar.plataform.api.dto.response.CarWashResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CarWashService {
    CarWashResponse create(CarWashRequest dto);
    List<CarWashResponse> findAllByOrderByCreatedAtAsc();
}