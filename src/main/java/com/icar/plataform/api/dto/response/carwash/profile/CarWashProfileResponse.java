package com.icar.plataform.api.dto.response.carwash.profile;

import java.util.List;
import java.util.UUID;

public record CarWashProfileResponse(
        UUID id,
        String name,
        String description,
        String coverPhoto
) {}
