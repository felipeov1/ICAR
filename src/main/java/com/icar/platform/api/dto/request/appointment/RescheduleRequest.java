package com.icar.platform.api.dto.request.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public record RescheduleRequest(
        @NotNull
        @Future
        ZonedDateTime newDateTime
) {}