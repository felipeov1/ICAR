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
import java.util.stream.Collectors;

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

        validateVehicleDetails(request.vehicleDetails());

        CarWashOffering offering = mapper.toEntity(request);
        offering.setProfile(profile);

        CarWashOffering saved = offeringRepository.save(offering);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public CarWashOfferingResponse update(UUID offeringId, CarWashOfferingRequest request) {
        CarWashOffering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        validateVehicleDetails(request.vehicleDetails());

        mapper.updateEntity(request, offering);

        CarWashOffering updated = offeringRepository.save(offering);
        return mapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashOfferingResponse> findAllByProfileId(UUID profileId) {
        return offeringRepository.findByProfile_Id(profileId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashOfferingResponse> findByProfileIdAndVehicleType(UUID profileId, String vehicleType) {
        return offeringRepository.findByProfile_Id(profileId).stream()
                .filter(CarWashOffering::isActive)
                .filter(offering -> offering.getVehicleDetails().containsKey(vehicleType))
                .map(mapper::toDto)
                .collect(Collectors.toList());
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


    private void validateVehicleDetails(Map<String, CarWashOfferingRequest.VehicleDetailRequest> details) {
        if (details == null || details.isEmpty()) {
            throw new BusinessException("Pelo menos um tipo de veículo deve ser especificado com preço e duração.");
        }

        details.forEach((type, detail) -> {
            if (detail.price() == null || detail.price().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("O preço deve ser maior que zero para o tipo de veículo: " + type);
            }
            if (detail.durationMinutes() == null || detail.durationMinutes() <= 0) {
                throw new BusinessException("A duração deve ser maior que zero para o tipo de veículo: " + type);
            }
        });
    }

    @Deprecated
    @Transactional
    public CarWashOfferingResponse createWithCarWashId(UUID carWashId, CarWashOfferingRequest request) {
        UUID profileId = profileRepository.findProfileIdByRegistrationId(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found"));
        return create(profileId, request);
    }

}