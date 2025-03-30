package com.icar.plataform.service.validation;

import com.icar.plataform.domain.enums.AppointmentStatus;
import com.icar.plataform.exception.BusinessException;
import com.icar.plataform.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewValidator {
    private final AppointmentRepository appointmentRepository;

    public void validateCustomerEligibility(UUID customerId, UUID carWashId) {
        boolean hasUsedService = appointmentRepository.existsByCustomerIdAndCarWashIdAndStatus(
                customerId,
                carWashId,
                AppointmentStatus.COMPLETED
        );

        if (!hasUsedService) {
            throw new BusinessException("Customer must have completed an appointment to leave a review");
        }
    }
}