package com.icar.platform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Map;

public record CarWashOfferingRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 500) String description,
        @NotBlank String serviceType,
        @NotEmpty Map<String, VehicleDetailRequest> vehicleDetails
) {
    public record VehicleDetailRequest(
            @NotNull BigDecimal price,
            @NotNull Integer durationMinutes
    ) {}
}