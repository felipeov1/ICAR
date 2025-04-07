package com.icar.plataform.infrastructure.validation.validator;

import com.icar.plataform.domain.enums.AppointmentStatus;
import com.icar.plataform.domain.model.Appointment;
import com.icar.plataform.api.dto.request.AppointmentRequest;
import com.icar.plataform.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentValidator {

    public void validateCreate(AppointmentRequest dto) {
        validateDateTime(dto.dateTime());
    }

    public void validateReschedule(Appointment appointment, LocalDateTime newDateTime) {
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new BusinessException("Only pending appointments can be rescheduled");
        }
        validateDateTime(newDateTime);
    }

    private void validateDateTime(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Appointment date cannot be in the past");
        }
    }


}