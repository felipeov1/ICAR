package com.icar.plataform.application.service.carwashprofile;

import com.icar.plataform.domain.model.carwash.CarWashProfile;
import com.icar.plataform.domain.repository.carwash.CarWashProfileRepository;
import com.icar.plataform.api.dto.request.carwash.CarWashProfileRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashProfileResponse;
import com.icar.plataform.api.mapper.carwash.CarWashProfileMapper;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarWashProfileServiceImpl implements CarWashProfileService {

    private final CarWashProfileRepository carWashProfileRepository;
    private final CarWashProfileMapper mapper;

    @Override
    public CarWashProfileResponse createProfile(UUID carWashId, CarWashProfileRequest request) {

        CarWashProfile carWashProfile = new CarWashProfile();
        carWashProfile.setCarWashId(carWashId);
        carWashProfile.setName(request.getName());
        carWashProfile.setDescription(request.getDescription());
        carWashProfile.setCoverPhoto(request.getCoverPhoto());
        carWashProfile.setPhotos(request.getPhotos());
        carWashProfile.setOpeningHours(request.getOpeningHours());
        carWashProfile.setVehicleTypes(request.getVehicleTypes());

        carWashProfile = carWashProfileRepository.save(carWashProfile);

        return mapper.toDto(carWashProfile);
    }

    @Override
    public CarWashProfileResponse getProfileByCarWashId(UUID carWashId) {
        return carWashProfileRepository.findByCarWashId(carWashId)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for CarWash ID: " + carWashId));
    }
}
