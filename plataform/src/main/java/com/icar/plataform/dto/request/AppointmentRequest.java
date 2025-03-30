package com.icar.plataform.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.icar.plataform.domain.enums.PaymentMethodType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequest(
        @NotNull(message = "Customer ID is required")
        UUID customerId,

        @NotNull(message = "Car Wash ID is required")
        UUID carWashId,

        @NotNull(message = "Service ID is required")
        UUID serviceId,

        @Future(message = "Appointment date must be in the future")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime dateTime,

        @NotNull(message = "Payment method is required")
        PaymentMethodType paymentMethod
) {}