package com.icar.plataform.api.dto.response.carwash;

import java.util.List;
import java.util.UUID;

public record CarWashProfileResponse(
        UUID id,
        String name,
        String description,
        String coverPhoto,
        List<String> photos,
        List<String> openingHours,
        List<String> vehicleTypes
) {}
