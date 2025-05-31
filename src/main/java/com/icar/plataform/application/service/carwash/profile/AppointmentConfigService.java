package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.AppointmentConfigRequest;
import com.icar.plataform.api.dto.response.carwash.profile.AppointmentConfigResponse;

import java.util.UUID;

public interface AppointmentConfigService {
    AppointmentConfigResponse getConfig(UUID carWashId);
    AppointmentConfigResponse createConfig(UUID carWashId, AppointmentConfigRequest request); // NOVO
    AppointmentConfigResponse updateConfig(UUID carWashId, AppointmentConfigRequest request);
}