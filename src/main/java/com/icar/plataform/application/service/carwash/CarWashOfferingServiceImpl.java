package com.icar.plataform.application.service.carwash;

import com.icar.plataform.api.dto.request.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.CarWashOfferingResponse;
import com.icar.plataform.api.mapper.CarWashOfferingMapper;
import com.icar.plataform.domain.model.CarWash;
import com.icar.plataform.domain.model.CarWashOffering;
import com.icar.plataform.domain.repository.CarWashRepository;
import com.icar.plataform.domain.repository.CarWashOfferingRepository;
import com.icar.plataform.shared.exception.DuplicateEntityException;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service // Changed from @org.springframework.stereotype.Service to avoid confusion
@RequiredArgsConstructor
public class CarWashOfferingServiceImpl implements CarWashOfferingService {
    private final CarWashOfferingRepository carWashOfferingRepository;
    private final CarWashRepository carWashRepository;
    private final CarWashOfferingMapper carWebserviceMapper;

    @Override
    @Transactional
    public CarWashOfferingResponse create(UUID carWashId, CarWashOfferingRequest request) {
        // 1. Get the CarWash entity - no .getCarWash() needed
        CarWash carWash = carWashRepository.findById(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash not found"));

        // 2. Check for duplicate service names
        if (carWashOfferingRepository.existsByCarWashAndNameIgnoreCase(carWash, request.name())) {
            throw new DuplicateEntityException(
                    "Service with this name already exists for this car wash",
                    "service",
                    "name"
            );
        }

        // 3. Create and save the new service
        CarWashOffering service = carWebserviceMapper.toEntity(request);
        service.setCarWash(carWash);  // Associate with the car wash

        CarWashOffering saved = carWashOfferingRepository.save(service);
        return carWebserviceMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CarWashOfferingResponse update(UUID serviceId, CarWashOfferingRequest request) {
        CarWashOffering service = carWashOfferingRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        carWebserviceMapper.updateEntity(request, service);
        CarWashOffering updated = carWashOfferingRepository.save(service);
        return carWebserviceMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deactivate(UUID serviceId) {
        CarWashOffering service = carWashOfferingRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        service.setActive(false);
        carWashOfferingRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashOfferingResponse> findAllByCarWash(UUID carWashId) {
        return carWashOfferingRepository.findByCarWashId(carWashId).stream()
                .map(carWebserviceMapper::toDto)
                .toList();
    }
}