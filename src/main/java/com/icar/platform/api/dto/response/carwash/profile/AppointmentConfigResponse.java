package com.icar.platform.api.dto.response.carwash.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentConfigResponse {
    private Integer minAdvanceNoticeMinutes;
    private Integer minEditNoticeMinutes;
    private Integer minCancelNoticeMinutes;
    private Integer maxAdvanceBookingDays;
    private Integer gapMinutes;
    private Boolean allowOvertime;
}
