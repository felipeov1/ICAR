package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.AppointmentConfigRequest;
import com.icar.platform.api.dto.response.carwash.profile.AppointmentConfigResponse;

import java.util.UUID;

public interface AppointmentConfigService {
    AppointmentConfigResponse getConfig(UUID carWashId);
    AppointmentConfigResponse createConfig(UUID carWashId, AppointmentConfigRequest request);
    AppointmentConfigResponse updateConfig(UUID carWashId, AppointmentConfigRequest request);
}