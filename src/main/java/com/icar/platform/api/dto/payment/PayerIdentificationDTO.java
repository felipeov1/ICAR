package com.icar.platform.api.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PayerIdentificationDTO {

    @NotNull
    private String type;

    @NotNull
    private String number;
}