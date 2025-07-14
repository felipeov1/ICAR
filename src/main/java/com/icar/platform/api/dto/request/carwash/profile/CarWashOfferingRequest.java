package com.icar.platform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
public record CarWashOfferingRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 500) String description,
        @NotNull @NotEmpty Map<String, BigDecimal> vehiclePrices,
        @NotNull @NotEmpty Map<String, Integer> vehicleEstimatedTimes,
        Boolean active,
        String serviceType
) {}