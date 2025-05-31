package com.icar.plataform.api.dto.response.carwash.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentConfigResponse {
    private Integer minAdvanceNoticeMinutes;
    private Integer minEditNoticeMinutes;
    private Integer minCancelNoticeMinutes;
}