package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.AppointmentConfigRequest;
import com.icar.plataform.api.dto.response.carwash.profile.AppointmentConfigResponse;
import com.icar.plataform.api.mapper.carwash.AppointmentConfigMapper;
import com.icar.plataform.domain.model.carwash.profile.AppointmentConfig;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.repository.carwash.profile.AppointmentConfigRepository;
import com.icar.plataform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.plataform.shared.exception.BusinessException;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
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
    public AppointmentConfigResponse getConfig(UUID carWashId) {
        return configRepository.findByProfile_CarWashRegistration_Id(carWashId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment config not found"));
    }

    @Override
    @Transactional
    public AppointmentConfigResponse createConfig(UUID carWashId, AppointmentConfigRequest request) {
        validateConfig(request);

        // Verifica se já existe configuração
        if (configRepository.existsByProfile_CarWashRegistration_Id(carWashId)) {
            throw new BusinessException("Appointment configuration already exists for this car wash");
        }

        CarWashProfile profile = profileRepository.findByCarWashRegistration_Id(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found"));

        AppointmentConfig config = mapper.toEntity(request);
        config.setProfile(profile);

        return mapper.toResponse(configRepository.save(config));
    }

    @Override
    @Transactional
    public AppointmentConfigResponse updateConfig(UUID carWashId, AppointmentConfigRequest request) {
        validateConfig(request);

        CarWashProfile profile = profileRepository.findByCarWashRegistration_Id(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found"));

        AppointmentConfig config = configRepository.findByProfile_CarWashRegistration_Id(carWashId)
                .orElseGet(() -> {
                    AppointmentConfig newConfig = new AppointmentConfig();
                    newConfig.setProfile(profile);
                    return newConfig;
                });

        mapper.updateFromRequest(request, config);
        return mapper.toResponse(configRepository.save(config));
    }

    private void validateConfig(AppointmentConfigRequest request) {
        if (request.getMinAdvanceNoticeMinutes() <= 0) {
            throw new BusinessException("Min advance notice must be positive");
        }
        if (request.getMinEditNoticeMinutes() <= 0) {
            throw new BusinessException("Min edit notice must be positive");
        }
        if (request.getMinCancelNoticeMinutes() <= 0) {
            throw new BusinessException("Min cancel notice must be positive");
        }
    }
}