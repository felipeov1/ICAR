package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.carwash.profile.VehicleType;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.domain.repository.carwash.profile.VehicleTypeRepository;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleTypesServiceImpl implements VehicleTypesService {

    private final VehicleTypeRepository vehicleTypeRepository;
    private final CarWashProfileRepository profileRepository;

    @Override
    @Transactional(readOnly = true)
    public List<String> getVehicleTypesByProfileId(UUID profileId) {
        if (!profileRepository.existsById(profileId)) {
            throw new ResourceNotFoundException("Car wash profile not found");
        }
        return vehicleTypeRepository.findVehicleTypesByProfileId(profileId);
    }

    @Override
    @Transactional
    public List<String> updateVehicleTypesByProfileId(UUID profileId, List<String> vehicleTypes) {
        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found"));

        vehicleTypeRepository.deleteByProfileId(profileId);

        vehicleTypes.forEach(type -> {
            VehicleType vehicleType = new VehicleType();
            vehicleType.setProfileId(profileId);
            vehicleType.setVehicleType(type);
            vehicleTypeRepository.save(vehicleType);
        });

        return vehicleTypes;
    }
}