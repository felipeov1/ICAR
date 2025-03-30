package com.icar.plataform.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.icar.plataform.domain.enums.AppointmentStatus;
import com.icar.plataform.domain.enums.PaymentMethodType;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        UUID customerId,
        String customerName,
        UUID carWashId,
        String carWashName,
        UUID serviceId,
        String serviceName,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime dateTime,

        AppointmentStatus status,
        PaymentMethodType paymentMethod,
        Double amountPaid,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime updatedAt
) {}