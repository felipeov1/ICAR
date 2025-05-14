package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.domain.model.carwash.profile.*;
import com.icar.plataform.domain.repository.carwash.profile.*;
import com.icar.plataform.api.dto.request.carwash.profile.*;
import com.icar.plataform.api.dto.response.carwash.profile.*;
import com.icar.plataform.api.mapper.carwash.CarWashProfileMapper;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CarWashProfileServiceImpl implements CarWashProfileService {

    private final CarWashProfileRepository profileRepository;
    private final CarWashProfileMapper mapper;

    @Override
    @Transactional
    public CarWashProfileResponse createProfile(UUID carWashId, CarWashProfileRequest request) {
        CarWashProfile profile = mapper.toEntity(request);
        profile.setCarWashId(carWashId);
        profile = profileRepository.save(profile);
        return mapper.toDto(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public CarWashProfileResponse getProfileByCarWashId(UUID carWashId) {
        CarWashProfile profile = profileRepository.findByCarWashId(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return mapper.toDto(profile);
    }

    @Override
    @Transactional
    public CarWashProfileResponse updateProfile(UUID carWashId, CarWashProfileRequest request) {
        CarWashProfile profile = profileRepository.findByCarWashId(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        profile.setName(request.getName());
        profile.setDescription(request.getDescription());
        profile.setCoverPhoto(request.getCoverPhoto());

        profile = profileRepository.save(profile);
        return mapper.toDto(profile);
    }
}