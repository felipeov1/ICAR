package com.icar.plataform.application.service.carwash;

import com.icar.plataform.api.dto.request.carwash.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashOfferingResponse;
import com.icar.plataform.api.mapper.carwash.CarWashOfferingMapper;
import com.icar.plataform.domain.model.carwash.CarWashOffering;
import com.icar.plataform.domain.model.carwash.CarWashProfile;
import com.icar.plataform.domain.repository.carwash.CarWashOfferingRepository;
import com.icar.plataform.domain.repository.carwash.CarWashProfileRepository;
import com.icar.plataform.shared.exception.DuplicateEntityException;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarWashOfferingServiceImpl implements CarWashOfferingService {

    private final CarWashOfferingRepository carWashOfferingRepository;
    private final CarWashProfileRepository carWashProfileRepository;
    private final CarWashOfferingMapper carWebserviceMapper;

    @Override
    @Transactional
    public CarWashOfferingResponse create(UUID carWashProfileId, CarWashOfferingRequest request) {
        CarWashProfile carWashProfile = carWashProfileRepository.findById(carWashProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found"));

        if (carWashOfferingRepository.existsByCarWashProfileAndNameIgnoreCase(carWashProfile, request.name())) {
            throw new DuplicateEntityException(
                    "Service with this name already exists for this car wash profile",
                    "service",
                    "name"
            );
        }

        CarWashOffering service = carWebserviceMapper.toEntity(request);
        service.setCarWashProfile(carWashProfile);

        CarWashOffering saved = carWashOfferingRepository.save(service);
        return carWebserviceMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashOfferingResponse> findAllByCarWashProfile(UUID carWashProfileId) {
        return carWashOfferingRepository.findByCarWashProfileId(carWashProfileId).stream()
                .map(carWebserviceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CarWashOfferingResponse update(UUID offeringId, CarWashOfferingRequest request) {
        CarWashOffering service = carWashOfferingRepository.findById(offeringId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        carWebserviceMapper.updateEntity(request, service);
        CarWashOffering updated = carWashOfferingRepository.save(service);
        return carWebserviceMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deactivate(UUID offeringId) {
        CarWashOffering service = carWashOfferingRepository.findById(offeringId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        service.setActive(false);
        carWashOfferingRepository.save(service);
    }
}

