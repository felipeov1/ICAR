package com.icar.platform.application.service.appointment;

import com.icar.platform.api.dto.request.appointment.AppointmentRequest;
import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.api.mapper.appointment.AppointmentMapper;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.enums.PaymentMethod;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.customer.CustomerAddress;
import com.icar.platform.domain.model.payment.coupon.AppliedCoupon;
import com.icar.platform.domain.model.payment.coupon.Coupon;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.domain.repository.carwash.offering.CarWashOfferingRepository;
import com.icar.platform.domain.repository.carwash.profile.AppointmentConfigRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.domain.repository.customer.CustomerAddressRepository;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.domain.repository.payment.coupon.AppliedCouponRepository;
import com.icar.platform.domain.repository.payment.coupon.CouponRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final CarWashAppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final CarWashProfileRepository profileRepository;
    private final CarWashOfferingRepository offeringRepository;
    private final CustomerAddressRepository addressRepository;
    private final AppointmentConfigRepository appointmentConfigRepository;
    private final AppointmentMapper appointmentMapper;
    private final CouponRepository couponRepository;
    private final AppliedCouponRepository appliedCouponRepository;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(UUID customerId, AppointmentRequest request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        CarWashProfile profile = profileRepository.findById(request.carwashId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil do Lava-rápido não encontrado"));
        CustomerAddress address = addressRepository.findByIdAndCustomerIdAndDeletedAtIsNull(request.addressId(), customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não pertence ao cliente"));
        String vehicleType = request.vehicleType();

        CarWashOffering mainOffering = offeringRepository.findById(request.offeringId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço principal não encontrado"));

        BigDecimal basePrice = mainOffering.getVehiclePrices().get(vehicleType);
        Integer baseTime = mainOffering.getVehicleEstimatedTimes().get(vehicleType);
        if (basePrice == null || baseTime == null) {
            throw new BusinessException("Serviço principal não possui preço ou tempo definido para o veículo: " + vehicleType);
        }

        BigDecimal totalPrice = basePrice;
        int totalTime = baseTime;

        List<CarWashOffering> extraOfferings = Collections.emptyList();
        if (request.extraOfferingIds() != null && !request.extraOfferingIds().isEmpty()) {
            extraOfferings = offeringRepository.findAllById(request.extraOfferingIds());
            for (CarWashOffering extra : extraOfferings) {
                BigDecimal extraPrice = extra.getVehiclePrices().get(vehicleType);
                Integer extraTime = extra.getVehicleEstimatedTimes().get(vehicleType);
                if (extraPrice != null && extraTime != null) {
                    totalPrice = totalPrice.add(extraPrice);
                    totalTime += extraTime;
                } else {
                    log.warn("Serviço extra ID {} ignorado por falta de preço/tempo para o veículo {}", extra.getId(), vehicleType);
                }
            }
        }

        validateAppointmentTime(request.startTime(), profile.getId(), totalTime);

        BigDecimal finalPrice = totalPrice;
        Coupon couponToApply = null;

        if (request.couponCode() != null && !request.couponCode().isBlank()) {
            log.info("Tentando aplicar o cupom: {} sobre o valor total de {}", request.couponCode(), totalPrice);
            Coupon coupon = couponRepository.findValidByCodeAndProfile(request.couponCode(), profile.getId())
                    .orElseThrow(() -> new BusinessException("Cupom inválido ou não encontrado."));

            validateCoupon(coupon, customerId, totalPrice);

            BigDecimal discount = calculateDiscount(coupon, totalPrice);
            finalPrice = totalPrice.subtract(discount).max(BigDecimal.ZERO);
            couponToApply = coupon;
            log.info("Cupom {} aplicado. Preço total: {}, Desconto: {}, Preço final: {}", coupon.getCode(), totalPrice, discount, finalPrice);
        }

        CarWashAppointment appointment = new CarWashAppointment();
        appointment.setCustomer(customer);
        appointment.setProfile(profile);
        appointment.setOffering(mainOffering);
        appointment.setAddress(address);
        appointment.setDateTime(request.startTime());
        appointment.setCarType(vehicleType);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setPaymentMethod(PaymentMethod.valueOf(request.paymentMethod()));
        appointment.setOriginalAmount(totalPrice);
        appointment.setAmountPaid(finalPrice);
        appointment.setTotalDurationMinutes(totalTime);

        if (!extraOfferings.isEmpty()) {
            appointment.setSelectedExtraServices(new HashSet<>(extraOfferings));
        }

        CarWashAppointment savedAppointment = appointmentRepository.save(appointment);

        if (couponToApply != null) {
            AppliedCoupon appliedCoupon = new AppliedCoupon();
            appliedCoupon.setAppointment(savedAppointment);
            appliedCoupon.setCoupon(couponToApply);
            appliedCoupon.setCustomer(customer);
            appliedCoupon.setOriginalAmount(totalPrice);
            appliedCoupon.setDiscountApplied(totalPrice.subtract(finalPrice));
            appliedCoupon.setFinalAmount(finalPrice);
             appliedCoupon.setAppliedAt(ZonedDateTime.now());
            appliedCouponRepository.save(appliedCoupon);
            couponToApply.setCurrentUses(couponToApply.getCurrentUses() + 1);
            couponRepository.save(couponToApply);
        }

        return appointmentMapper.toResponse(savedAppointment);
    }

    private void validateAppointmentTime(ZonedDateTime appointmentTime, UUID profileId, Integer estimatedTime) {
        AppointmentConfig config = appointmentConfigRepository.findByProfile_Id(profileId)
                .orElseThrow(() -> new BusinessException("Configuração de agendamento não encontrada."));

        ZonedDateTime earliestBookingTime = ZonedDateTime.now().plusMinutes(config.getMinAdvanceNoticeMinutes());
        if (appointmentTime.isBefore(earliestBookingTime)) {
            throw new BusinessException("O agendamento deve ser feito com pelo menos " +
                    config.getMinAdvanceNoticeMinutes() + " minutos de antecedência.");
        }

        ZonedDateTime appointmentEndTime = appointmentTime.plusMinutes(estimatedTime);

        List<CarWashAppointment> conflictingAppointments = appointmentRepository.findConflictingAppointments(
                profileId,
                AppointmentStatus.CANCELED,
                appointmentTime,
                appointmentEndTime
        );

        if (!conflictingAppointments.isEmpty()) {
            throw new BusinessException("Este horário já está ocupado ou em conflito. Por favor, escolha outro.");
        }
    }

    private void validateCoupon(Coupon coupon, UUID customerId, BigDecimal originalPrice) {
         if (coupon.getValidUntil().isBefore(ZonedDateTime.now())) {
             throw new BusinessException("Este cupom está expirado.");
         }
         if (coupon.getValidFrom().isAfter(ZonedDateTime.now())) {
             throw new BusinessException("Este cupom ainda não é válido.");
         }
        if (coupon.getMaxUses() != null && coupon.getCurrentUses() >= coupon.getMaxUses()) {
            throw new BusinessException("Este cupom atingiu o limite máximo de usos.");
        }
        if (coupon.getMinOrderValue() != null && originalPrice.compareTo(coupon.getMinOrderValue()) < 0) {
            throw new BusinessException("O valor do pedido não atinge o mínimo de R$" + coupon.getMinOrderValue() + " para usar este cupom.");
        }
        if (coupon.getMaxUsesPerUser() != null) {
            long usesByCustomer = appliedCouponRepository.countByCouponIdAndCustomerId(coupon.getId(), customerId);
            if (usesByCustomer >= coupon.getMaxUsesPerUser()) {
                throw new BusinessException("Você já utilizou o limite máximo para este cupom.");
            }
        }
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal originalPrice) {
        if (coupon.getDiscountValue() != null) {
            return coupon.getDiscountValue();
        }
        if (coupon.getDiscountPercentage() != null) {
            return originalPrice.multiply(coupon.getDiscountPercentage().divide(new BigDecimal("100")));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<AppointmentResponse> getUpcomingAppointmentsForCustomer(UUID customerId) {
        return appointmentRepository.findUpcomingByCustomerId(customerId, ZonedDateTime.now())
                .stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getCompletedOrCanceledAppointmentsForCustomer(UUID customerId) {
        return appointmentRepository.findHistoryByCustomerId(customerId)
                .stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(UUID appointmentId, UUID customerId) {
        CarWashAppointment appointment = appointmentRepository.findByIdAndCustomerId(appointmentId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));

        validateActionDeadline(appointment, "cancel");

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointment.setUpdatedAt(ZonedDateTime.now());

        CarWashAppointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(updatedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse completeAppointment(UUID appointmentId) {
        CarWashAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setUpdatedAt(ZonedDateTime.now());
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentResponse rescheduleAppointment(UUID appointmentId, UUID customerId, ZonedDateTime newDateTime) {
        CarWashAppointment appointment = appointmentRepository.findByIdAndCustomerId(appointmentId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));

        validateActionDeadline(appointment, "edit");

        Integer totalDuration = appointment.getTotalDurationMinutes();
        if (totalDuration == null || totalDuration <= 0) {
            totalDuration = appointment.getOffering().getVehicleEstimatedTimes().get(appointment.getCarType());
        }

        validateAppointmentTime(newDateTime, appointment.getProfile().getId(), totalDuration);

        appointment.setDateTime(newDateTime);
        appointment.setUpdatedAt(ZonedDateTime.now());

        CarWashAppointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(updatedAppointment);
    }

    private void validateActionDeadline(CarWashAppointment appointment, String action) {
        AppointmentConfig config = appointmentConfigRepository.findByProfile_Id(appointment.getProfile().getId())
                .orElseThrow(() -> new BusinessException("Configuração de agendamento não encontrada."));

        Integer noticeMinutes = action.equals("cancel") ? config.getMinCancelNoticeMinutes() : config.getMinEditNoticeMinutes();
        String actionText = action.equals("cancel") ? "cancelado" : "editado";

        if (noticeMinutes == null || noticeMinutes <= 0) return;

        ZonedDateTime deadline = appointment.getDateTime().minusMinutes(noticeMinutes);

        if (ZonedDateTime.now().isAfter(deadline)) {
            throw new BusinessException("O prazo para esta ação expirou. Para " + actionText +
                    ", entre em contato com o profissional: " + appointment.getProfile().getCarWashRegistration().getPhone());
        }
    }

    @Override
    public List<AppointmentResponse> getUpcomingAppointmentsForCarWash(UUID carWashId) {
        return appointmentRepository.findUpcomingByCarWashId(carWashId, ZonedDateTime.now())
                .stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getCompletedAppointmentsForCarWash(UUID carWashId) {
        return appointmentRepository.findByCarWashIdAndStatus(carWashId, AppointmentStatus.COMPLETED)
                .stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getCanceledAppointmentsForCarWash(UUID carWashId) {
        return appointmentRepository.findByCarWashIdAndStatus(carWashId, AppointmentStatus.CANCELED)
                .stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }
}