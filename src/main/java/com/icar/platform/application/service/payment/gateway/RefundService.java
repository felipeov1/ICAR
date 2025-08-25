package com.icar.platform.application.service.payment.gateway;

import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.enums.CreationChannel;
import com.icar.platform.domain.enums.PaymentMethod;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.payment.gateway.CompanyMercadoPagoConfig;
import com.icar.platform.domain.model.payment.gateway.PaymentTransaction;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.domain.repository.payment.gateway.CompanyMercadoPagoConfigRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import com.mercadopago.resources.payment.PaymentRefund;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundService {

    private final CarWashAppointmentRepository appointmentRepository;
    private final CompanyMercadoPagoConfigRepository configRepository;
    private final PixPaymentService pixPaymentService;

    private static final ZoneId BRASILIA_ZONE_ID = ZoneId.of("America/Sao_Paulo");

    @Transactional
    public void processRefundForAppointment(UUID appointmentId, UUID profileId) {
        log.info("Iniciando processo de reembolso para agendamento {} pelo perfil {}", appointmentId, profileId);

        CarWashAppointment appointment = appointmentRepository.findByIdAndProfileId(appointmentId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado ou não pertence a esta empresa."));

        validateRefundEligibility(appointment);

        PaymentTransaction transaction = appointment.getPaymentTransaction();
        if (transaction == null) {
            throw new BusinessException("Agendamento não possui transação de pagamento para reembolso.");
        }

        UUID companyId = appointment.getProfile().getCarWashRegistration().getId();
        CompanyMercadoPagoConfig companyConfig = configRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new BusinessException("A empresa não possui configuração do Mercado Pago."));

        PaymentRefund refund = pixPaymentService.createTotalRefund(
                transaction.getMercadoPagoPaymentId(),
                companyConfig.getAccessToken()
        );

        if (refund != null && ("approved".equals(refund.getStatus()) || "pending".equals(refund.getStatus()))) {
            appointment.setStatus(AppointmentStatus.CANCELED);
            appointment.setUpdatedAt(LocalDateTime.now(BRASILIA_ZONE_ID));
            appointmentRepository.save(appointment);
            log.info("Reembolso processado com sucesso para o agendamento {}. Status do reembolso: {}. Status do agendamento atualizado para CANCELED.",
                    appointment.getId(), refund.getStatus());

        } else {
            log.error("Falha ao processar o reembolso para o pagamento MP ID: {}. O status do agendamento não foi alterado.", transaction.getMercadoPagoPaymentId());
            throw new BusinessException("Não foi possível processar o reembolso através do Mercado Pago neste momento. Tente novamente mais tarde.");
        }
    }

    private void validateRefundEligibility(CarWashAppointment appointment) {
        boolean isEligibleStatus = appointment.getStatus() == AppointmentStatus.REFUND_PENDING ||
                appointment.getStatus() == AppointmentStatus.CONFIRMED;

        if (!isEligibleStatus) {
            throw new BusinessException("O agendamento não está em um status que permite reembolso (Pendente de Reembolso ou Confirmado).");
        }

        if (appointment.getPaymentMethod() != PaymentMethod.PLATFORM) {
            throw new BusinessException("Este agendamento não foi pago pela plataforma e não pode ser reembolsado.");
        }

        if (appointment.getCreationChannel() == CreationChannel.MANUAL) {
            throw new BusinessException("Agendamentos manuais não podem ser reembolsados pela plataforma.");
        }
    }
}