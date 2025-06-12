package com.icar.plataform.infrastructure.validation.validator.appointment;

import com.icar.plataform.api.dto.request.appointment.AppointmentRequest;
import com.icar.plataform.domain.enums.AppointmentStatus;
import com.icar.plataform.domain.model.appointment.CarWashAppointment;
import com.icar.plataform.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentValidator {

    public void validateCreate(AppointmentRequest request) {
        if (request.dateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("A data do agendamento não pode ser no passado");
        }
    }


}