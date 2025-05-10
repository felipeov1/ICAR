package com.icar.plataform.application.service.carwash;

import com.icar.plataform.api.dto.request.carwash.CarWashRegistrationRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashRegistrationResponse;
import com.icar.plataform.api.mapper.carwash.CarWashRegistrationMapper;
import com.icar.plataform.domain.model.carwash.CarWashRegistration;
import com.icar.plataform.domain.repository.carwash.CarWashRegistrationDataRepository;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarWashRegistrationServiceImpl implements CarWashRegistrationService {

    private final CarWashRegistrationDataRepository repository;
    private final CarWashRegistrationMapper mapper;

    @Override
    @Transactional
    public CarWashRegistrationResponse create(CarWashRegistrationRequest request) {
        CarWashRegistration carWashRegistration = mapper.toEntity(request);
        CarWashRegistration saved = repository.save(carWashRegistration);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CarWashRegistrationResponse findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashRegistrationResponse> findAllByOrderByCreatedAtAsc() {
        return repository.findAllByOrderByCreatedAtAsc().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}