package com.icar.plataform.application.service.carwash;

import com.icar.plataform.domain.model.CarWash;
import com.icar.plataform.api.dto.request.CarWashRequest;
import com.icar.plataform.api.dto.response.CarWashResponse;
import com.icar.plataform.api.mapper.CarWashMapper;
import com.icar.plataform.domain.repository.CarWashRepository;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarWashServiceImpl implements CarWashService {

    private final CarWashRepository repository;
    private final CarWashMapper mapper;

    @Override
    @Transactional
    public CarWashResponse create(CarWashRequest request) {
        CarWash carWash = mapper.toEntity(request);
        CarWash saved = repository.save(carWash);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CarWashResponse findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashResponse> findAllByOrderByCreatedAtAsc() {
        return repository.findAllByOrderByCreatedAtAsc().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}