package com.icar.platform.api.dto.response.carwash.profile;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record CarWashOfferingResponse(
        UUID id,
        String name,
        String description,
        String serviceType,
        boolean active,
        Map<String, VehicleDetailResponse> vehicleDetails
) {
    public record VehicleDetailResponse(
            BigDecimal price,
            Integer durationMinutes
    ) {}
}