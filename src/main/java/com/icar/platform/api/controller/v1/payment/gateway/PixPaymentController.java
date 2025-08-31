package com.icar.platform.api.controller.v1.payment.gateway;

import com.icar.platform.api.dto.payment.AppointmentPaymentRequestDTO;
import com.icar.platform.api.dto.payment.PixPaymentDTO;
import com.icar.platform.api.dto.payment.PixPaymentResponseDTO;
import com.icar.platform.application.service.customer.CustomerProfileService;
import com.icar.platform.application.service.payment.gateway.PaymentOrchestrationService;
import com.icar.platform.application.service.payment.gateway.PixPaymentService;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentRefund;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "Payments")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PixPaymentController {

    private final PixPaymentService pixPaymentService;
    private final PaymentOrchestrationService paymentOrchestrationService;
    private final CustomerProfileService customerProfileService;
    private final CarWashAppointmentRepository appointmentRepository;

    @PostMapping("/appointments/{appointmentId}/payments")
    @Operation(summary = "Cria um pagamento PIX para um agendamento existente")
    public ResponseEntity<PixPaymentResponseDTO> createPaymentForAppointment(
            @PathVariable UUID appointmentId,
            @Valid @RequestBody AppointmentPaymentRequestDTO paymentRequest) {

        log.info("Controller: Requisição de pagamento para agendamento {} com saveCpf={}", appointmentId, paymentRequest.isSaveCpfForFutureUse());
        log.info("Controller: Recebida requisição de pagamento para agendamento {}. Payload: {}", appointmentId, paymentRequest);

        log.info("Controller: Requisição de pagamento para agendamento {} com saveCpf={}", appointmentId, paymentRequest.isSaveCpfForFutureUse());
        if (paymentRequest.isSaveCpfForFutureUse()) {
            UUID customerId = appointmentRepository.findCustomerIdById(appointmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado para obter o cliente."));

            customerProfileService.updateCustomerCpf(customerId, paymentRequest.getCpf());
        }

        PixPaymentResponseDTO paymentDetails = paymentOrchestrationService.createPixPaymentForAppointment(
                appointmentId,
                paymentRequest.getCpf(),
                paymentRequest.getDeviceId()
        );
        return ResponseEntity.ok(paymentDetails);
    }



    @PutMapping("/payments/{paymentId}/cancel")
    public ResponseEntity<Payment> cancelPendingPayment(@PathVariable Long paymentId) {
        Payment canceledPayment = pixPaymentService.cancelPendingPayment(paymentId);
        return ResponseEntity.ok(canceledPayment);
    }

    @PostMapping("/payments/{paymentId}/refunds/partial")
    public ResponseEntity<PaymentRefund> createPartialRefund(
            @PathVariable Long paymentId,
            @RequestParam BigDecimal amount) {
        PaymentRefund refund = pixPaymentService.createPartialRefund(paymentId, amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(refund);
    }
}