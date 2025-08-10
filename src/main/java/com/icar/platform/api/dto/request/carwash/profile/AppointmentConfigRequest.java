package com.icar.platform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentConfigRequest {
    private Integer minAdvanceNoticeMinutes;
    private Integer minEditNoticeMinutes;
    private Integer minCancelNoticeMinutes;
    private Integer gapMinutes;
    @NotNull
    private Boolean allowOvertime;
}