package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashScheduleConfigRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleService {
    void setScheduleConfiguration(UUID carWashId, CarWashScheduleConfigRequest request);
    List<LocalDateTime> getAvailableSlots(UUID carWashId, LocalDate date);
}
