package com.icar.platform.api.dto.response.carwash.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CarWashOfferingResponse(
        @NotNull UUID id,
        @NotBlank @Size(max = 100) String name,
        @Size(max = 500) String description,
        @NotNull BigDecimal price,
        @NotNull Integer estimatedTime,
        @NotNull Boolean active,
        String statusText,
        String serviceType,
        Map<String, BigDecimal> vehiclePrices,
        Map<String, Integer> vehicleEstimatedTimes
) {}