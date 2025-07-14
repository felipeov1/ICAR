package com.icar.platform.application.service.carwash.offering;

import com.icar.platform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashOfferingResponse;
import com.icar.platform.api.mapper.carwash.CarWashOfferingMapper;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.repository.carwash.offering.CarWashOfferingRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarWashOfferingServiceImpl implements CarWashOfferingService {

    private final CarWashOfferingRepository offeringRepository;
    private final CarWashProfileRepository profileRepository;
    private final CarWashOfferingMapper mapper;


    @Override
    @Transactional
    public CarWashOfferingResponse create(UUID profileId, CarWashOfferingRequest request) {
        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil do lava-rápido não encontrado"));

        if (offeringRepository.existsByProfileIdAndNameIgnoreCase(profileId, request.name())) {
            throw new DuplicateEntityException("Já existe um serviço com este nome", "offering", "name");
        }

        validateVehicleDetails(request.vehiclePrices(), request.vehicleEstimatedTimes());

        CarWashOffering offering = mapper.toEntity(request);
        offering.setProfile(profile);
        offering.setActive(request.active() != null ? request.active() : true);


        if (request.serviceType() != null && !request.serviceType().isBlank()) {
            offering.setServiceType(request.serviceType().toUpperCase());
        } else {
            offering.setServiceType("PRINCIPAL");
        }

        CarWashOffering saved = offeringRepository.save(offering);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public CarWashOfferingResponse update(UUID offeringId, CarWashOfferingRequest request) {
        CarWashOffering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        validateVehicleDetails(request.vehiclePrices(), request.vehicleEstimatedTimes());

        mapper.updateEntity(request, offering);

        if (request.active() != null) {
            offering.setActive(request.active());
        }

        if (request.serviceType() != null && !request.serviceType().isBlank()) {
            offering.setServiceType(request.serviceType().toUpperCase());
        }

        CarWashOffering updated = offeringRepository.save(offering);
        return mapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashOfferingResponse> findAllByProfileId(UUID profileId) {
        return offeringRepository.findByProfile_Id(profileId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashOfferingResponse> findByProfileIdAndVehicleType(UUID profileId, String vehicleType) {
        return offeringRepository.findByProfile_Id(profileId).stream()
                .filter(offering -> offering.getVehiclePrices().containsKey(vehicleType))
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deactivate(UUID offeringId) {
        CarWashOffering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));
        offering.setActive(false);
        offeringRepository.save(offering);
    }

    @Override
    @Transactional
    public void activate(UUID offeringId) {
        CarWashOffering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));
        offering.setActive(true);
        offeringRepository.save(offering);
    }


    private void validateVehicleDetails(Map<String, BigDecimal> prices, Map<String, Integer> times) {
        if (prices.size() != times.size() || !prices.keySet().equals(times.keySet())) {
            throw new BusinessException("Vehicle types must match between prices and estimated times");
        }

        if (prices.isEmpty()) {
            throw new BusinessException("At least one vehicle type must be specified");
        }

        prices.forEach((type, price) -> {
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Price must be greater than zero for vehicle type: " + type);
            }
        });

        times.forEach((type, time) -> {
            if (time <= 0) {
                throw new BusinessException("Estimated time must be greater than zero for vehicle type: " + type);
            }
        });
    }

    private String getStatusText(CarWashOffering offering) {
        if (offering.getDeletedAt() != null) return "Deleted";
        return offering.isActive() ? "Active" : "Inactive";
    }

    @Deprecated
    @Transactional
    public CarWashOfferingResponse createWithCarWashId(UUID carWashId, CarWashOfferingRequest request) {
        UUID profileId = profileRepository.findProfileIdByRegistrationId(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found"));
        return create(profileId, request);
    }

}