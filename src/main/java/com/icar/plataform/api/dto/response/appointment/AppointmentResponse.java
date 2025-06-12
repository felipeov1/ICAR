package com.icar.plataform.api.dto.response.appointment;

import com.icar.plataform.domain.enums.AppointmentStatus;
import com.icar.plataform.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        UUID customerId,
        String customerName,
        UUID carWashId,
        String carWashName,
        UUID offeringId,
        String serviceName,
        AddressSummaryResponse address,  // endereço resumido aqui
        String carType,
        LocalDateTime dateTime,
        AppointmentStatus status,
        PaymentMethod paymentMethod,
        BigDecimal amountPaid,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
