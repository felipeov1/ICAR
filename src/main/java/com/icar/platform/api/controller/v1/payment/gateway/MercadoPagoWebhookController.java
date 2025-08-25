package com.icar.platform.api.controller.v1.payment.gateway;

import com.icar.platform.api.dto.payment.MercadoPagoNotificationDTO;
import com.icar.platform.application.service.payment.gateway.MercadoPagoWebhookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Payments")
@RequestMapping("/api/v1/notifications/mercado-pago")
@RequiredArgsConstructor
public class MercadoPagoWebhookController {

    private final MercadoPagoWebhookService webhookService;

    @PostMapping
    public ResponseEntity<Void> handleWebhookNotification(@RequestBody MercadoPagoNotificationDTO notification, HttpServletRequest request) {
        log.info("Recebida notificação do Mercado Pago: {}", notification.getAction());

        String signatureHeader = request.getHeader("x-signature");
        String requestIdHeader = request.getHeader("x-request-id");

        webhookService.processWebhook(notification, signatureHeader, requestIdHeader);

        return ResponseEntity.ok().build();
    }
}