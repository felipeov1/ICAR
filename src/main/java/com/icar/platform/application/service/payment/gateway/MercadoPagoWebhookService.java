package com.icar.platform.application.service.payment.gateway;

import com.icar.platform.api.dto.payment.MercadoPagoNotificationDTO;
import com.icar.platform.application.service.notification.NotificationService;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.payment.gateway.PaymentTransaction;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.domain.repository.payment.gateway.PaymentTransactionRepository;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant; // <-- IMPORT NECESSÁRIO
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MercadoPagoWebhookService {

    @Value("${mercadopago.webhook-secret-key}")
    private String webhookSecretKey;
    private final PaymentTransactionRepository transactionRepository;
    private final CarWashAppointmentRepository appointmentRepository;
    private final NotificationService notificationService;
    private static final long SIGNATURE_TOLERANCE_MS = 600000;

    @Async
    @Transactional
    public void processWebhook(MercadoPagoNotificationDTO notification, String signatureHeader, String requestIdHeader) {
        if (!isSignatureValid(notification, signatureHeader, requestIdHeader)) {
            log.warn("Assinatura do Webhook inválida. A notificação será ignorada. Action: {}", notification.getAction());
            return;
        }

        log.info("Assinatura do Webhook validada com sucesso.");

        if ("payment".equals(notification.getType())) {
            processPaymentNotification(Long.valueOf(notification.getData().getId()));
        } else {
            log.info("Tipo de notificação ignorada: {}", notification.getType());
        }
    }


    @Transactional
    private void processPaymentNotification(Long paymentId) {
        try {
            // NOTA: Para o SDK do Mercado Pago funcionar corretamente no servidor,
            // ele precisa do Access Token. Vamos garantir que ele está sendo configurado.
            // Esta parte da lógica depende de como você obtém o Access Token do vendedor.
            // Assumindo que a transação tenha o appointment e o appointment tenha o perfil.
            PaymentTransaction tempTransaction = transactionRepository.findByMercadoPagoPaymentIdWithAppointment(paymentId)
                    .orElseThrow(() -> new RuntimeException("Transação não encontrada para obter Access Token"));
            String sellerAccessToken = tempTransaction.getAppointment().getProfile().getCarWashRegistration().getMercadoPagoConfig().getAccessToken();
            com.mercadopago.MercadoPagoConfig.setAccessToken(sellerAccessToken);

            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(paymentId);

            PaymentTransaction transaction = transactionRepository.findByMercadoPagoPaymentIdWithAppointment(paymentId)
                    .orElse(null);

            if (transaction == null) {
                log.warn("Transação com ID de pagamento {} não encontrada no banco de dados. Webhook ignorado.", paymentId);
                return;
            }

            CarWashAppointment appointment = transaction.getAppointment();
            if (appointment == null) {
                log.error("ERRO CRÍTICO: A transação {} não está associada a nenhum agendamento.", transaction.getId());
                return;
            }

            transaction.setStatus(payment.getStatus());
            transactionRepository.save(transaction);

            if (appointment.getStatus() == AppointmentStatus.PENDING_PAYMENT) {
                if ("approved".equals(payment.getStatus())) {
                    appointment.setStatus(AppointmentStatus.CONFIRMED);
                    appointmentRepository.save(appointment);

                    log.info("Pagamento {} aprovado. Agendamento {} confirmado.", paymentId, appointment.getId());
                    notificationService.createNotificationForNewAppointment(appointment);
                } else if ("cancelled".equals(payment.getStatus()) || "rejected".equals(payment.getStatus())) {
                    appointment.setStatus(AppointmentStatus.PAYMENT_FAILED);
                    appointmentRepository.save(appointment);

                    log.warn("Pagamento {} foi '{}'. Agendamento {} marcado como falha no pagamento.",
                            paymentId, payment.getStatus(), appointment.getId());
                }
            } else {
                log.info("Status do agendamento {} já é '{}'. Nenhuma alteração feita pelo webhook.", appointment.getId(), appointment.getStatus());
            }

        } catch (MPException | MPApiException e) {
            log.error("Erro ao buscar detalhes do pagamento {} no Mercado Pago via webhook.", paymentId, e);
        }
    }

    private boolean isSignatureValid(MercadoPagoNotificationDTO notification, String signatureHeader, String requestIdHeader) {
        if (signatureHeader == null || signatureHeader.isEmpty()) {
            return false;
        }
        try {
            String[] parts = signatureHeader.split(",");
            String tsPart = parts[0].split("=")[1];
            String v1Part = parts[1].split("=")[1];
            long timestampNotificacao = Long.parseLong(tsPart);

            // <-- MUDANÇA 1: Usando Instant.now() para garantir que a hora está em UTC
            long timestampServidor = Instant.now().getEpochSecond();

            // <-- MUDANÇA 2: Adicionado log de depuração detalhado
            log.info("Validando timestamp. Servidor (UTC): {}, Notificação (UTC): {}, Diferença (s): {}",
                    timestampServidor,
                    timestampNotificacao,
                    (timestampServidor - timestampNotificacao));

            if ((timestampServidor - timestampNotificacao) > (SIGNATURE_TOLERANCE_MS / 1000)) {
                log.warn("Timestamp da notificação é muito antigo.");
                return false;
            }

            String signedTemplate = String.format("id:%s;request-id:%s;ts:%s;",
                    notification.getData().getId(),
                    requestIdHeader,
                    tsPart
            );
            String hmac = new HmacUtils("HmacSHA256", webhookSecretKey).hmacHex(signedTemplate);
            return Objects.equals(hmac, v1Part);
        } catch (Exception e) {
            log.error("Erro ao validar assinatura do webhook", e);
            return false;
        }
    }
}
