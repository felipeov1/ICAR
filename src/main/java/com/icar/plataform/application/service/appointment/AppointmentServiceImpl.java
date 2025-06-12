package com.icar.plataform.application.service.appointment;

import com.icar.plataform.api.dto.request.appointment.AppointmentRequest;
import com.icar.plataform.api.dto.response.appointment.AppointmentResponse;
import com.icar.plataform.api.mapper.appointment.AppointmentMapper;
import com.icar.plataform.domain.enums.AppointmentStatus;
import com.icar.plataform.domain.enums.PaymentMethod;
import com.icar.plataform.domain.model.appointment.CarWashAppointment;
import com.icar.plataform.domain.model.customer.Customer;
import com.icar.plataform.domain.model.customer.CustomerAddress;
import com.icar.plataform.domain.model.payment.coupon.AppliedCoupon;
import com.icar.plataform.domain.model.payment.coupon.Coupon;
import com.icar.plataform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.plataform.domain.repository.carwash.offering.CarWashOfferingRepository;
import com.icar.plataform.domain.repository.carwash.profile.AppointmentConfigRepository;
import com.icar.plataform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.plataform.domain.repository.customer.CustomerAddressRepository;
import com.icar.plataform.domain.repository.customer.CustomerRepository;
import com.icar.plataform.domain.repository.payment.coupon.AppliedCouponRepository;
import com.icar.plataform.domain.repository.payment.coupon.CouponRepository;
import com.icar.plataform.infrastructure.validation.validator.appointment.AppointmentValidator;
import com.icar.plataform.infrastructure.validation.validator.payment.CouponValidator;
import com.icar.plataform.shared.exception.BusinessException;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final CarWashAppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CarWashProfileRepository carWashProfileRepository;
    private final CarWashOfferingRepository offeringRepository;
    private final AppointmentConfigRepository appointmentConfigRepository;
    private final CouponRepository couponRepository;
    private final AppliedCouponRepository appliedCouponRepository;
    private final AppointmentValidator appointmentValidator;
    private final CouponValidator couponValidator;
    private final AppointmentMapper appointmentMapper;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(UUID customerId, AppointmentRequest request) {
        // Validação básica do agendamento
        appointmentValidator.validateCreate(request);

        // Buscar entidades relacionadas
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        // Buscar endereço do cliente (modificado para usar o método correto)
        CustomerAddress address = customerAddressRepository
                .findByIdAndCustomerIdAndDeletedAtIsNull(request.addressId(), customerId)
                .orElseThrow(() -> new BusinessException("Endereço não encontrado ou não pertence ao cliente"));


        var carWashProfile = carWashProfileRepository.findByCarWashRegistration_Id(request.carWashId())
                .orElseThrow(() -> new ResourceNotFoundException("Lava-rápido não encontrado"));

        var offering = offeringRepository.findByIdAndCarWashId(request.offeringId(), request.carWashId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado para este lava-rápido"));

        var config = appointmentConfigRepository.findByProfile_CarWashRegistration_Id(request.carWashId())
                .orElseThrow(() -> new BusinessException("Configurações de agendamento não encontradas para este lava-rápido"));

        // Validação de horário
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minAdvanceTime = now.plusMinutes(config.getMinAdvanceNoticeMinutes());
        if (request.dateTime().isBefore(minAdvanceTime)) {
            throw new BusinessException("O agendamento deve ser feito com pelo menos " +
                    config.getMinAdvanceNoticeMinutes() + " minutos de antecedência");
        }

        // Verificar disponibilidade do horário
        LocalDateTime endTime = request.dateTime().plusMinutes(offering.getEstimatedTime());
        boolean isSlotAvailable = !appointmentRepository.existsByCarWashIdAndDateTimeBetween(
                request.carWashId(), request.dateTime(), endTime);

        if (!isSlotAvailable) {
            throw new BusinessException("Horário indisponível para agendamento");
        }

        // Processar cupom de desconto se fornecido
        BigDecimal finalPrice = offering.getPrice();
        Coupon appliedCoupon = null;

        if (request.couponCode() != null && !request.couponCode().isEmpty()) {
            appliedCoupon = processCoupon(request.couponCode(), customer.getId(), carWashProfile.getId(), offering.getPrice());
            finalPrice = calculateFinalPrice(offering.getPrice(), appliedCoupon);
        }

        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(request.paymentMethod().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessException("Método de pagamento inválido");
        }

        // Criar agendamento
        CarWashAppointment appointment = appointmentMapper.toEntity(request);
        appointment.setCustomer(customer);
        appointment.setProfile(carWashProfile);
        appointment.setOffering(offering);
        appointment.setAddress(address); // Definindo o endereço
        appointment.setCarType(request.carType()); // Definindo o tipo de veículo
        appointment.setAmountPaid(finalPrice);
        appointment.setPaymentMethod(paymentMethod);
        appointment.setStatus(AppointmentStatus.CONFIRMED); // Confirmação automática

        CarWashAppointment savedAppointment = appointmentRepository.save(appointment);

        // Registrar uso do cupom se aplicado
        if (appliedCoupon != null) {
            registerCouponUsage(savedAppointment, appliedCoupon, customer, offering.getPrice(), finalPrice);
        }

        return appointmentMapper.toDto(savedAppointment);
    }

    private Coupon processCoupon(String couponCode, UUID customerId, UUID profileId, BigDecimal originalPrice) {
        LocalDateTime now = LocalDateTime.now();

        // Buscar cupom válido
        Coupon coupon = couponRepository.findValidCouponByCode(couponCode, now)
                .orElseThrow(() -> new BusinessException("Cupom inválido ou expirado"));

        // Validar cupom
        couponValidator.validateCoupon(coupon, customerId, profileId, originalPrice);

        // Verificar limite por usuário
        if (coupon.getMaxUsesPerUser() != null) {
            int userUses = couponRepository.countUsesByCustomer(coupon.getId(), customerId);
            if (userUses >= coupon.getMaxUsesPerUser()) {
                throw new BusinessException("Limite de usos deste cupom por usuário atingido");
            }
        }

        return coupon;
    }

    private BigDecimal calculateFinalPrice(BigDecimal originalPrice, Coupon coupon) {
        if (coupon.getDiscountValue() != null) {
            return originalPrice.subtract(coupon.getDiscountValue()).max(BigDecimal.ZERO);
        } else if (coupon.getDiscountPercentage() != null) {
            BigDecimal discount = originalPrice.multiply(
                    coupon.getDiscountPercentage().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            return originalPrice.subtract(discount).max(BigDecimal.ZERO);
        }
        return originalPrice;
    }

    private void registerCouponUsage(CarWashAppointment appointment, Coupon coupon,
                                     Customer customer, BigDecimal originalPrice, BigDecimal finalPrice) {
        AppliedCoupon appliedCoupon = new AppliedCoupon();
        appliedCoupon.setAppointment(appointment);
        appliedCoupon.setCoupon(coupon);
        appliedCoupon.setCustomer(customer);
        appliedCoupon.setOriginalAmount(originalPrice);
        appliedCoupon.setFinalAmount(finalPrice);
        appliedCoupon.setDiscountApplied(originalPrice.subtract(finalPrice));
        appliedCoupon.setAppliedAt(LocalDateTime.now());

        // Atualizar contador de usos do cupom
        coupon.setCurrentUses(coupon.getCurrentUses() + 1);
        couponRepository.save(coupon);

        // Salvar registro do cupom aplicado
        appliedCouponRepository.save(appliedCoupon);
    }


    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(UUID appointmentId, UUID customerId) {
        CarWashAppointment appointment = appointmentRepository.findByIdAndCustomerId(appointmentId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        var config = appointmentConfigRepository.findByProfile_CarWashRegistration_Id(appointment.getProfile().getCarWashRegistration().getId())
                .orElseThrow(() -> new BusinessException("Configurações de agendamento não encontradas"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minCancelTime = appointment.getDateTime().minusMinutes(config.getMinCancelNoticeMinutes());

        if (now.isAfter(minCancelTime)) {
            throw new BusinessException("O cancelamento deve ser feito com pelo menos " +
                    config.getMinCancelNoticeMinutes() + " minutos de antecedência");
        }

        // Verificar e reverter cupons usando o repositório
        List<AppliedCoupon> appliedCoupons = appliedCouponRepository.findByAppointmentId(appointmentId);
        if (!appliedCoupons.isEmpty()) {
            revertCouponUsage(appointment);
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointment.setUpdatedAt(now);

        CarWashAppointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updatedAppointment);
    }

    private void revertCouponUsage(CarWashAppointment appointment) {
        List<AppliedCoupon> appliedCoupons = appliedCouponRepository.findByAppointmentId(appointment.getId());

        for (AppliedCoupon appliedCoupon : appliedCoupons) {
            Coupon coupon = appliedCoupon.getCoupon();
            coupon.setCurrentUses(coupon.getCurrentUses() - 1);
            couponRepository.save(coupon);
            appliedCouponRepository.delete(appliedCoupon);
        }
    }

    @Override
    @Transactional
    public AppointmentResponse rescheduleAppointment(UUID appointmentId, UUID customerId, LocalDateTime newDateTime) {
        CarWashAppointment appointment = appointmentRepository.findByIdAndCustomerId(appointmentId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        LocalDateTime endTime = newDateTime.plusMinutes(appointment.getOffering().getEstimatedTime());
        boolean isSlotAvailable = !appointmentRepository.existsByCarWashIdAndDateTimeBetweenAndIdNot(
                appointment.getProfile().getCarWashRegistration().getId(), newDateTime, endTime, appointmentId);

        if (!isSlotAvailable) {
            throw new BusinessException("Novo horário indisponível para reagendamento");
        }

        appointment.setDateTime(newDateTime);
        appointment.setUpdatedAt(LocalDateTime.now());

        CarWashAppointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updatedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse completeAppointment(UUID appointmentId) {
        CarWashAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new BusinessException("Somente agendamentos confirmados podem ser completados");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setUpdatedAt(LocalDateTime.now());

        CarWashAppointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updatedAppointment);
    }


    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getUpcomingAppointmentsForCarWash(UUID carWashId) {
        List<CarWashAppointment> appointments = appointmentRepository
                .findUpcomingConfirmedAppointmentsForCarWash(carWashId, LocalDateTime.now());

        return appointments.stream().map(appointmentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getCompletedAppointmentsForCarWash(UUID carWashId) {
        List<CarWashAppointment> appointments = appointmentRepository
                .findCompletedAppointmentsForCarWash(carWashId);
        return appointments.stream().map(appointmentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getCanceledAppointmentsForCarWash(UUID carWashId) {
        List<CarWashAppointment> appointments = appointmentRepository
                .findCanceledAppointmentsForCarWash(carWashId);
        return appointments.stream().map(appointmentMapper::toDto).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getUpcomingAppointmentsForCustomer(UUID customerId) {
        return appointmentRepository.findUpcomingConfirmedAppointmentsForCustomer(customerId, LocalDateTime.now())
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getCompletedOrCanceledAppointmentsForCustomer(UUID customerId) {
        List<CarWashAppointment> appointments = appointmentRepository.getCompletedOrCanceledAppointmentsForCustomer(customerId);

        return appointments.stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }


}