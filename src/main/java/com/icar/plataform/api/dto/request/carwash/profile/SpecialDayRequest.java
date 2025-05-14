package com.icar.plataform.api.dto.request.carwash.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialDayRequest {
    private String date; // formato "yyyy-MM-dd"
    private String startTime; // formato "HH:mm" (opcional)
    private String endTime; // formato "HH:mm" (opcional)
    private boolean isHoliday;
    private String reason;
}
