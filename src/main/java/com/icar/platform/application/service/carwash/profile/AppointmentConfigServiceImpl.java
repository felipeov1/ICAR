package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.AppointmentConfigRequest;
import com.icar.platform.api.dto.response.carwash.profile.AppointmentConfigResponse;
import com.icar.platform.api.mapper.carwash.AppointmentConfigMapper;
import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.repository.carwash.profile.AppointmentConfigRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentConfigServiceImpl implements AppointmentConfigService {

    private final AppointmentConfigRepository configRepository;
    private final CarWashProfileRepository profileRepository;
    private final AppointmentConfigMapper mapper;

    @Override
    public AppointmentConfigResponse getConfig(UUID profileId) {
        return configRepository.findByProfile_Id(profileId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment config not found"));
    }

    @Override
    @Transactional
    public AppointmentConfigResponse createConfig(UUID profileId, AppointmentConfigRequest request) {
        validateConfig(request);

        if (configRepository.existsByProfile_CarWashRegistration_Id(profileId)) {
            throw new BusinessException("Appointment configuration already exists for this profile");
        }

        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found"));

        AppointmentConfig config = mapper.toEntity(request);
        config.setProfile(profile);

        return mapper.toResponse(configRepository.save(config));
    }

    @Override
    @Transactional
    public AppointmentConfigResponse updateConfig(UUID profileId, AppointmentConfigRequest request) {
        validateConfig(request);

        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found"));

        AppointmentConfig config = configRepository.findByProfile_Id(profileId)
                .orElseGet(() -> {
                    AppointmentConfig newConfig = new AppointmentConfig();
                    newConfig.setProfile(profile);
                    return newConfig;
                });

        mapper.updateFromRequest(request, config);
        return mapper.toResponse(configRepository.save(config));
    }

    @Deprecated
    public AppointmentConfigResponse getConfigByCarWashId(UUID carWashId) {
        UUID profileId = profileRepository.findProfileIdByRegistrationId(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found"));
        return getConfig(profileId);
    }

    private void validateConfig(AppointmentConfigRequest request) {
        if (request.getMinAdvanceNoticeMinutes() < 0) {
            throw new BusinessException("Min advance notice must be positive");
        }
        if (request.getMinEditNoticeMinutes() < 0) {
            throw new BusinessException("Min edit notice must be positive");
        }
        if (request.getMinCancelNoticeMinutes() < 0) {
            throw new BusinessException("Min cancel notice must be positive");
        }
    }
}