package com.icar.plataform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentConfigRequest {

    @Positive(message = "Min advance notice must be positive")
    private Integer minAdvanceNoticeMinutes;

    @Positive(message = "Min edit notice must be positive")
    private Integer minEditNoticeMinutes;

    @Positive(message = "Min cancel notice must be positive")
    private Integer minCancelNoticeMinutes;
}