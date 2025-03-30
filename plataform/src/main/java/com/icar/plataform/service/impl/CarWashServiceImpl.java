package com.icar.plataform.service.impl;

import com.icar.plataform.domain.model.CarWash;
import com.icar.plataform.dto.request.CarWashRequest;
import com.icar.plataform.dto.response.CarWashResponse;
import com.icar.plataform.mapper.CarWashMapper;
import com.icar.plataform.repository.CarWashRepository;
import com.icar.plataform.service.CarWashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarWashServiceImpl implements CarWashService {

    private final CarWashRepository repository;
    private final CarWashMapper mapper;

    @Override
    @Transactional
    public CarWashResponse create(CarWashRequest dto) {
        CarWash carWash = mapper.toEntity(dto);
        CarWash saved = repository.save(carWash);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashResponse> findNearby(Double latitude, Double longitude, Double radiusKm) {
        return repository.findByLocationNear(latitude, longitude, radiusKm)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}