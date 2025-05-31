package com.icar.plataform.application.service.carwash.offering;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashOfferingResponse;
import com.icar.plataform.api.mapper.carwash.CarWashOfferingMapper;
import com.icar.plataform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.plataform.domain.model.carwash.offering.CarWashOffering;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.plataform.domain.repository.carwash.offering.CarWashOfferingRepository;
import com.icar.plataform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.plataform.shared.exception.BusinessException;
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
    private final CarWashOfferingMapper carWashOfferingMapper;
    private final CarWashProfileRepository carWashProfileRepository;
    private final CarWashRegistrationDataRepository registrationRepository;

    @Override
    @Transactional
    public CarWashOfferingResponse create(UUID carWashId, CarWashOfferingRequest request) {
        // Verifica se o registro existe
        if (!registrationRepository.existsById(carWashId)) {
            throw new ResourceNotFoundException("Registro de lava-rápido não encontrado");
        }

        // Busca o profile associado ao carWashId
        CarWashProfile profile = carWashProfileRepository.findByCarWashRegistration_Id(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de lava-rápido não encontrado"));

        // Verifica se já existe um serviço com o mesmo nome
        if (carWashOfferingRepository.existsByCarWashIdAndNameIgnoreCaseAndDeletedAtIsNull(carWashId, request.name())) {
            throw new DuplicateEntityException(
                    "name",
                    "Já existe um serviço com este nome para este lava-rápido",
                    "DUPLICATE_OFFERING_NAME"
            );
        }

        CarWashOffering offering = carWashOfferingMapper.toEntity(request);
        offering.setProfile(profile);

        if (request.active() == null) {
            offering.setActive(true);
        } else {
            offering.setActive(request.active());
        }

        CarWashOffering saved = carWashOfferingRepository.save(offering);
        return carWashOfferingMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashOfferingResponse> findAllByCarWashId(UUID carWashId) {
        return carWashOfferingRepository.findByCarWashIdAndDeletedAtIsNull(carWashId).stream()
                .map(carWashOfferingMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CarWashOfferingResponse update(UUID offeringId, CarWashOfferingRequest request) {
        CarWashOffering offering = carWashOfferingRepository.findByIdAndDeletedAtIsNull(offeringId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        carWashOfferingMapper.updateEntity(request, offering);

        if (request.active() != null) {
            offering.setActive(request.active());
        }

        CarWashOffering updated = carWashOfferingRepository.save(offering);
        return carWashOfferingMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deactivate(UUID offeringId) {
        CarWashOffering offering = carWashOfferingRepository.findById(offeringId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        if (!offering.isActive()) {
            throw new BusinessException("O serviço já está desativado");
        }

        offering.setActive(false);
        carWashOfferingRepository.save(offering);
    }

    @Override
    @Transactional
    public void activate(UUID offeringId) {
        CarWashOffering offering = carWashOfferingRepository.findByIdAndDeletedAtIsNull(offeringId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        offering.setActive(true);
        carWashOfferingRepository.save(offering);
    }

}