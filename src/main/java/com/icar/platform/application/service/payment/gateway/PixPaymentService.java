package com.icar.platform.application.service.payment.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.icar.platform.api.dto.payment.PixPaymentResponseDTO;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.payment.gateway.PaymentTransaction;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.MercadoPagoException;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentRefund;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PixPaymentService {

    private final CarWashAppointmentRepository appointmentRepository;

    @Value("${app.api-url}")
    private String apiBaseUrl;
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public PixPaymentResponseDTO processPixPayment(
            CarWashAppointment appointment,
            BigDecimal totalTransactionAmount,
            String cpf,
            String sellerAccessToken,
            String statementDescriptor,
            String deviceId) {
        try {
            MercadoPagoConfig.setAccessToken(sellerAccessToken);
            Customer customer = appointment.getCustomer();

            if (customer == null) {
                throw new BusinessException("Agendamento sem cliente associado.");
            }

            if (cpf == null || cpf.isBlank()) {
                throw new MercadoPagoException("O CPF do pagador é obrigatório para pagamentos PIX.");
            }

            PaymentClient client = new PaymentClient();

            IdentificationRequest identificationRequest = IdentificationRequest.builder()
                    .type("CPF")
                    .number(cpf)
                    .build();

            String[] nameParts = customer.getFullName().trim().split("\\s+");
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ?
                    Arrays.stream(nameParts).skip(1).collect(Collectors.joining(" ")) : "";

            PaymentPayerRequest payerRequest = PaymentPayerRequest.builder()
                    .email(customer.getEmail())
                    .firstName(firstName)
                    .lastName(lastName)
                    .identification(identificationRequest)
                    .build();

            List<PaymentItemRequest> items = new ArrayList<>();
            for (CarWashOffering service : appointment.getSelectedServices()) {
                BigDecimal unitPrice = service.getVehicleDetails().get(appointment.getCarType()).getPrice();
                PaymentItemRequest item = PaymentItemRequest.builder()
                        .id(service.getId().toString())
                        .title(service.getName())
                        .description(service.getDescription())
                        .categoryId("car_wash_services")
                        .quantity(1)
                        .unitPrice(unitPrice)
                        .build();
                items.add(item);
            }

            PaymentAdditionalInfoRequest additionalInfo = PaymentAdditionalInfoRequest.builder()
                    .items(items)
                    .payer(PaymentAdditionalInfoPayerRequest.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .build())
                    .build();

            String notificationUrl = apiBaseUrl + "/api/v1/notifications/mercado-pago";

            PaymentCreateRequest createRequest = PaymentCreateRequest.builder()
                    .transactionAmount(totalTransactionAmount)
                    .description("Serviços em " + appointment.getProfile().getCarWashRegistration().getTradeName())
                    .externalReference(appointment.getId().toString())
                    .notificationUrl(notificationUrl)
                    .paymentMethodId("pix")
                    .payer(payerRequest)
                    .additionalInfo(additionalInfo)
                    .statementDescriptor(statementDescriptor)
                    .dateOfExpiration(OffsetDateTime.now().plusMinutes(30))
                    .build();


            Map<String, String> customHeaders = new HashMap<>();
            if (deviceId != null && !deviceId.isBlank()) {
                customHeaders.put("X-meli-session-id", deviceId);
            }

            MPRequestOptions requestOptions = MPRequestOptions.builder()
                    .customHeaders(customHeaders)
                    .build();

            log.info("Enviando requisição de pagamento para o Mercado Pago para o agendamento {}", appointment.getId());
            Payment createdPayment = client.create(createRequest, requestOptions);

            if (createdPayment.getPointOfInteraction() == null || createdPayment.getPointOfInteraction().getTransactionData() == null) {
                throw new BusinessException("Resposta do gateway de pagamento inválida. Não contém dados do PIX.");
            }

            PaymentTransaction newTransaction = new PaymentTransaction();
            newTransaction.setMercadoPagoPaymentId(createdPayment.getId());
            newTransaction.setStatus(createdPayment.getStatus());
            newTransaction.setAppointment(appointment);
            appointment.setPaymentTransaction(newTransaction);
            appointmentRepository.save(appointment);

            String qrCodeBase64 = createdPayment.getPointOfInteraction().getTransactionData().getQrCodeBase64();
            String qrCode = createdPayment.getPointOfInteraction().getTransactionData().getQrCode();

            return new PixPaymentResponseDTO(
                    createdPayment.getId(),
                    createdPayment.getStatus(),
                    createdPayment.getStatusDetail(),
                    qrCodeBase64,
                    qrCode,
                    createdPayment.getExternalReference()
            );

        } catch (MPApiException e) {
            log.error("Erro da API do Mercado Pago: {}", e.getApiResponse().getContent());
            throw new BusinessException("Falha ao gerar o pagamento PIX. Por favor, tente novamente.");
        } catch (MPException e) {
            log.error("Erro no SDK do Mercado Pago: {}", e.getMessage());
            throw new BusinessException("Ocorreu um erro interno ao processar o pagamento.");
        }
    }

    public Payment cancelPendingPayment(Long paymentId) {
        try {
            PaymentClient client = new PaymentClient();
            Payment canceledPayment = client.cancel(paymentId);
            log.info("Pagamento pendente {} cancelado com sucesso.", paymentId);
            return canceledPayment;
        } catch (MPException | MPApiException e) {
            handleException(e, paymentId);
            return null;
        }
    }

    public PaymentRefund createTotalRefund(Long paymentId, String sellerAccessToken) {
        try {
            MercadoPagoConfig.setAccessToken(sellerAccessToken);

            PaymentClient client = new PaymentClient();

            PaymentRefund refund = client.refund(paymentId);

            log.info("Reembolso total para o pagamento {} criado com sucesso. Status: {}", paymentId, refund.getStatus());
            return refund;

        } catch (MPException | MPApiException e) {
            handleException(e, paymentId);
            return null;
        }
    }


    public PaymentRefund createPartialRefund(Long paymentId, BigDecimal amount) {
        try {
            PaymentClient client = new PaymentClient();
            PaymentRefund refund = client.refund(paymentId, amount);

            log.info("Reembolso parcial de {} para o pagamento {} criado com sucesso. Status: {}", amount, paymentId, refund.getStatus());
            return refund;
        } catch (MPException | MPApiException e) {
            handleException(e, paymentId);
            return null;
        }
    }


    private MPRequestOptions buildRequestOptions(String accessToken) {
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("x-idempotency-key", UUID.randomUUID().toString());
        return MPRequestOptions.builder()
                .accessToken(accessToken)
                .customHeaders(customHeaders)
                .build();
    }

    private PaymentPayerRequest buildPayerRequest(CarWashAppointment appointment, String payerCpf) {
        Customer customer = appointment.getCustomer();
        if (customer == null) {
            throw new IllegalStateException("O agendamento deve ter um cliente associado para processar o pagamento.");
        }

        String[] nameParts = customer.getFullName().trim().split("\\s+");
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ?
                Arrays.stream(nameParts).skip(1).collect(Collectors.joining(" ")) : "";

        String identificationNumber = (payerCpf != null && !payerCpf.isBlank()) ? payerCpf : customer.getIdentificationNumber();

        if (identificationNumber == null || identificationNumber.isBlank()) {
            throw new MercadoPagoException("O CPF do pagador é obrigatório para pagamentos PIX.");
        }

        return PaymentPayerRequest.builder()
                .email(customer.getEmail())
                .firstName(firstName)
                .lastName(lastName)
                .entityType("individual")
                .identification(
                        IdentificationRequest.builder()
                                .type("CPF")
                                .number(identificationNumber)
                                .build())
                .build();
    }

    private void handleException(Exception e, Long resourceId) {
        if (e instanceof MPApiException apiException) {
            String requestId = "N/A";
            if (apiException.getApiResponse() != null && apiException.getApiResponse().getHeaders() != null) {
                requestId = apiException.getApiResponse().getHeaders().getOrDefault("x-request-id", List.of("N/A")).getFirst();
            }
            log.error("Erro da API do Mercado Pago. Status: {}, Causa: {}, Request-ID: {}",
                    apiException.getStatusCode(), apiException.getApiResponse().getContent(), requestId);
            throw new MercadoPagoException(apiException.getApiResponse().getContent());

        } else {
            log.error("Erro inesperado do SDK do Mercado Pago ao operar no recurso {}: {}", resourceId, e.getMessage());
            throw new MercadoPagoException(e.getMessage());
        }
    }
}