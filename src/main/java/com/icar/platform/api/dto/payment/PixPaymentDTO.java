package com.icar.platform.api.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PixPaymentDTO {

    @NotNull(message = "O ID da empresa é obrigatório.")
    private UUID companyId;

    @NotNull
    private BigDecimal transactionAmount;

    @NotNull
    @JsonProperty("description")
    private String productDescription;

    @NotNull
    @Valid
    private PayerDTO payer;

    @NotBlank(message = "O CPF é obrigatório para pagamentos com PIX.")
    private String cpf;

    private boolean saveCpfForFutureUse;
}