package com.icar.plataform.service;

import com.icar.plataform.dto.request.CarWashRequest;
import com.icar.plataform.dto.response.CarWashResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarWashService {
    CarWashResponse create(CarWashRequest dto);
    List<CarWashResponse> findNearby(Double latitude, Double longitude, Double radiusKm);
}