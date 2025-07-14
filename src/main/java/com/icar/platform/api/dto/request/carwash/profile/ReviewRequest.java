package com.icar.platform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record ReviewRequest(
        @NotNull UUID appointmentId,
        @Min(1) @Max(5) Integer rating,
        String comment
) {}
