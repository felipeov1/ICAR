package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.plataform.api.mapper.carwash.CarWashProfileMapper;
import com.icar.plataform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.plataform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.plataform.shared.exception.DuplicateEntityException;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarWashProfileServiceImpl implements CarWashProfileService {

    private final CarWashProfileRepository profileRepository;
    private final CarWashRegistrationDataRepository registrationRepository;
    private final CarWashProfileMapper mapper;

    @Override
    @Transactional
    public CarWashProfileResponse createProfile(UUID carWashId, CarWashProfileRequest request) {
        CarWashRegistration registration = registrationRepository.findById(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash registration not found"));

        if (profileRepository.existsByCarWashRegistration_Id(carWashId)) {
            throw new DuplicateEntityException("Profile already exists for this car wash", "car_wash_profile", "car_wash_id");
        }


        CarWashProfile profile = mapper.toEntity(request);

        CarWashRegistration carWashRegistration = new CarWashRegistration();
        carWashRegistration.setId(carWashId);

        profile.setCarWashRegistration(carWashRegistration);

        profile = profileRepository.save(profile);

        return mapper.toDto(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public CarWashProfileResponse getProfileByCarWashId(UUID carWashId) {
        CarWashProfile profile = profileRepository.findByCarWashRegistration_Id(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return mapper.toDto(profile);
    }


    @Override
    @Transactional
    public CarWashProfileResponse updateProfile(UUID carWashId, CarWashProfileRequest request) {
        CarWashProfile profile = profileRepository.findByCarWashRegistration_Id(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        mapper.updateEntity(request, profile);
        profile = profileRepository.save(profile);
        return mapper.toDto(profile);
    }
}