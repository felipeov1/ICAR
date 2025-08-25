package com.icar.platform.api.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PixPaymentResponseDTO {
    private Long id;
    private String status;
    private String detail;
    private String qrCodeBase64;
    private String qrCode;
    private String externalReference;

    public PixPaymentResponseDTO(Long id, String status, String detail, String qrCodeBase64, String qrCode, String externalReference) {
        this.id = id;
        this.status = status;
        this.detail = detail;
        this.qrCodeBase64 = qrCodeBase64;
        this.qrCode = qrCode;
        this.externalReference = externalReference;
    }
}