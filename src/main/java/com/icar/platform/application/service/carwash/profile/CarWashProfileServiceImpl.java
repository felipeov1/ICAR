package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.platform.api.mapper.carwash.CarWashProfileMapper;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.shared.exception.DuplicateEntityException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarWashProfileServiceImpl implements CarWashProfileService {

    private final CarWashProfileRepository profileRepository;
    private final CarWashRegistrationDataRepository registrationRepository;
    private final CarWashProfileMapper mapper;

    @Override
    @Transactional
    public CarWashProfileResponse createProfile(@NonNull UUID carWashId, @NonNull CarWashProfileRequest request) {
        CarWashRegistration registration = registrationRepository.findById(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash registration not found"));

        if (profileRepository.findProfileIdByRegistrationId(carWashId).isPresent()) {
            throw new DuplicateEntityException("Profile already exists for this car wash", "car_wash_profile", "car_wash_id");
        }

        CarWashProfile profile = mapper.toEntity(request);
        profile.setCarWashRegistration(registration);
        return mapper.toDto(profileRepository.save(profile));
    }

    @Override
    @Transactional(readOnly = true)
    public CarWashProfileResponse getProfileById(@NonNull UUID profileId) {
        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return mapper.toDto(profile);
    }

    @Override
    @Transactional
    public CarWashProfileResponse updateProfile(@NonNull UUID profileId, @NonNull CarWashProfileRequest request) {
        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        mapper.updateEntity(request, profile);
        return mapper.toDto(profileRepository.save(profile));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashProfileResponse> findAllForMarketplace() {
        return profileRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


}