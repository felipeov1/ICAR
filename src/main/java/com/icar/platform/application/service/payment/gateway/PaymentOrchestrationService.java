package com.icar.platform.application.service.payment.gateway;

import com.icar.platform.api.dto.payment.PixPaymentResponseDTO;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.coupon.AppliedCoupon;
import com.icar.platform.domain.model.payment.gateway.CompanyMercadoPagoConfig;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.domain.repository.payment.gateway.CompanyMercadoPagoConfigRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ConflictException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentOrchestrationService {

    private final PixPaymentService pixPaymentService;
    private final CarWashAppointmentRepository appointmentRepository;
    private final CompanyMercadoPagoConfigRepository configRepository;

    @Value("${icar.marketplace.convenience-fee:0.99}")
    private BigDecimal convenienceFee;

    @Transactional
    public PixPaymentResponseDTO createPixPaymentForAppointment(UUID appointmentId, String cpf) {
        log.info("Orquestrando pagamento para o agendamento {}", appointmentId);

        CarWashAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado com ID: " + appointmentId));

        if (appointment.getStatus() != AppointmentStatus.PENDING_PAYMENT) {
            log.warn("Tentativa de pagamento para agendamento {} com status inválido: {}", appointmentId, appointment.getStatus());
            throw new ConflictException("Este agendamento não está pendente de pagamento. Status atual: " + appointment.getStatus());
        }

        BigDecimal serviceValue = appointment.getOriginalAmount();
        BigDecimal couponDiscount = appointment.getAppliedCoupons().stream()
                .map(AppliedCoupon::getDiscountApplied)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal finalAmountForCustomer = serviceValue.add(convenienceFee).subtract(couponDiscount)
                .setScale(2, RoundingMode.HALF_UP);

        finalAmountForCustomer = finalAmountForCustomer.max(BigDecimal.ONE);

        UUID companyId = appointment.getProfile().getCarWashRegistration().getId();

        CompanyMercadoPagoConfig companyConfig = configRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new BusinessException("A empresa não conectou sua conta do Mercado Pago ainda."));

        String sellerAccessToken = companyConfig.getAccessToken();

        log.info("Valores calculados para Agendamento {}. Valor do serviço: {}, Cupom: {}, Taxa Conveniência: {}, Valor Final Cliente: {}",
                appointment.getId(), serviceValue, couponDiscount, convenienceFee, finalAmountForCustomer);

        appointment.setAmountPaid(finalAmountForCustomer);

        return pixPaymentService.processPixPayment(
                appointment,
                finalAmountForCustomer,
                cpf,
                sellerAccessToken
        );
    }
}