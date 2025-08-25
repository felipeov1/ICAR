package com.icar.platform.api.dto.response.appointment;

import com.icar.platform.api.dto.payment.PixPaymentResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppointmentCreationResponse {
    private AppointmentResponse appointment;
    private PixPaymentResponseDTO paymentDetails;
}
