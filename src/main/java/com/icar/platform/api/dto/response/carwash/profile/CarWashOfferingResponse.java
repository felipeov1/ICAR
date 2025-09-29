package com.icar.platform.api.dto.response.carwash.profile;

import com.icar.platform.domain.enums.DirtLevel;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record CarWashOfferingResponse(
        UUID id,
        String name,
        String description,
        String serviceType,
        String modality,
        boolean active,
        DirtLevel dirtLevelRecommendation,

        Map<String, VehicleDetailResponse> vehicleDetails
) {
    public record VehicleDetailResponse(
            BigDecimal price,
            Integer durationMinutes
    ) {}
}