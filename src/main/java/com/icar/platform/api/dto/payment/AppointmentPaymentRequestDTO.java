package com.icar.platform.api.dto.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentPaymentRequestDTO {
    @NotBlank(message = "O CPF é obrigatório.")
    private String cpf;
    private boolean saveCpfForFutureUse;
    private String deviceId;
}