package com.icar.plataform.api.dto.request.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequest(
        @NotNull(message = "ID do lava-rápido é obrigatório")
        UUID carWashId,

        @NotNull(message = "ID do serviço é obrigatório")
        UUID offeringId,

        @NotNull(message = "ID do endereço é obrigatório")
        UUID addressId,  // Novo campo

        @NotBlank(message = "Tipo do veículo é obrigatório")
        String carType,  // Novo campo

        @NotNull(message = "Data e hora do agendamento são obrigatórias")
        LocalDateTime dateTime,

        String couponCode,  // Opcional

        @NotNull(message = "Uma forma de pagamento é ogrigatório")
        String paymentMethod
) {}