package com.icar.platform.application.service.carwash.profile;

import java.util.List;
import java.util.UUID;

public interface VehicleTypesService {
    List<String> getVehicleTypesByProfileId(UUID profileId);
    List<String> updateVehicleTypesByProfileId(UUID profileId, List<String> vehicleTypes);
}