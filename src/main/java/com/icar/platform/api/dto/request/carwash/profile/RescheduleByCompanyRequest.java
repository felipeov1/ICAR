package com.icar.platform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.NotBlank;

public record RescheduleByCompanyRequest(
        @NotBlank(message = "A data não pode ser vazia.")
        String date,

        @NotBlank(message = "A hora não pode ser vazia.")
        String time
) {}
