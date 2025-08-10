package com.icar.platform.application.service.appointment;

import com.icar.platform.api.dto.request.appointment.AppointmentRequest;
import com.icar.platform.api.dto.request.appointment.ManualAppointmentRequest;
import com.icar.platform.api.dto.request.carwash.profile.RescheduleByCompanyRequest;
import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.api.dto.response.carwash.CompanyAppointmentResponse;
import com.icar.platform.api.mapper.appointment.AppointmentMapper;
import com.icar.platform.application.service.notification.NotificationService;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.enums.CreationChannel;
import com.icar.platform.domain.enums.PaymentMethod;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import com.icar.platform.domain.model.carwash.offering.VehicleOfferingDetail;
import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.carwash.profile.CompanyCustomer;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.customer.CustomerAddress;
import com.icar.platform.domain.model.payment.coupon.AppliedCoupon;
import com.icar.platform.domain.model.payment.coupon.Coupon;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.domain.repository.carwash.offering.CarWashOfferingRepository;
import com.icar.platform.domain.repository.carwash.profile.AppointmentConfigRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.domain.repository.carwash.profile.CompanyCustomerRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private static final ZoneId BRASILIA_ZONE_ID = ZoneId.of("America/Sao_Paulo");

    private final CarWashAppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final CarWashProfileRepository profileRepository;
    private final CarWashOfferingRepository offeringRepository;
    private final CustomerAddressRepository addressRepository;
    private final AppointmentConfigRepository appointmentConfigRepository;
    private final AppointmentMapper appointmentMapper;
    private final CouponRepository couponRepository;
    private final AppliedCouponRepository appliedCouponRepository;
    private final CompanyCustomerRepository companyCustomerRepository;
    private final NotificationService notificationService;

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

        if (request.offeringIds() == null || request.offeringIds().isEmpty()) {
            throw new BusinessException("Pelo menos um serviço deve ser selecionado.");
        }
        List<CarWashOffering> selectedOfferings = offeringRepository.findAllById(request.offeringIds());
        if (selectedOfferings.isEmpty() || selectedOfferings.size() != request.offeringIds().size()) {
            throw new ResourceNotFoundException("Um ou mais serviços selecionados são inválidos ou não foram encontrados.");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalTime = 0;

        for (CarWashOffering service : selectedOfferings) {
            VehicleOfferingDetail detail = service.getVehicleDetails().get(vehicleType);
            if (detail != null && detail.getPrice() != null && detail.getEstimatedTime() != null) {
                totalPrice = totalPrice.add(detail.getPrice());
                totalTime += detail.getEstimatedTime();
            } else {
                log.warn("Serviço ID {} ignorado por falta de preço/tempo para o veículo {}", service.getId(), vehicleType);
                throw new BusinessException("O serviço '" + service.getName() + "' não está disponível para o tipo de veículo selecionado.");
            }
        }

        if (totalTime <= 0) {
            throw new BusinessException("A duração total dos serviços é inválida. O agendamento não pode ser criado.");
        }

        validateAppointmentTime(request.startTime(), profile.getId(), totalTime);

        BigDecimal finalPrice = totalPrice;
        Coupon couponToApply = null;

        if (request.couponCode() != null && !request.couponCode().isBlank()) {
            if (!PaymentMethod.PLATFORM.name().equals(request.paymentMethod())) {
                throw new BusinessException("Cupons de desconto são válidos apenas para pagamentos na plataforma.");
            }

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
        appointment.setAddress(address);
        appointment.setDateTime(request.startTime());
        appointment.setCarType(vehicleType);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setPaymentMethod(PaymentMethod.valueOf(request.paymentMethod()));
        appointment.setOriginalAmount(totalPrice);
        appointment.setAmountPaid(finalPrice);
        appointment.setTotalDurationMinutes(totalTime);
        appointment.setSelectedServices(new HashSet<>(selectedOfferings));
        appointment.setCreationChannel(CreationChannel.MARKETPLACE);

        CarWashAppointment savedAppointment = appointmentRepository.save(appointment);

        notificationService.createNotificationForNewAppointment(savedAppointment);

        if (couponToApply != null) {
            AppliedCoupon appliedCoupon = new AppliedCoupon();
            appliedCoupon.setAppointment(savedAppointment);
            appliedCoupon.setCoupon(couponToApply);
            appliedCoupon.setCustomer(customer);
            appliedCoupon.setOriginalAmount(totalPrice);
            appliedCoupon.setDiscountApplied(totalPrice.subtract(finalPrice));
            appliedCoupon.setFinalAmount(finalPrice);
            appliedCoupon.setAppliedAt(LocalDateTime.now(BRASILIA_ZONE_ID));
            appliedCouponRepository.save(appliedCoupon);
            couponToApply.setCurrentUses(couponToApply.getCurrentUses() + 1);
            couponRepository.save(couponToApply);
        }

        return appointmentMapper.toResponse(savedAppointment);
    }

    private void validateAppointmentTime(LocalDateTime appointmentTime, UUID profileId, Integer estimatedTime) {
        validateAppointmentTime(appointmentTime, profileId, estimatedTime, null);
    }

    private void validateAppointmentTime(LocalDateTime appointmentTime, UUID profileId, Integer estimatedTime, UUID appointmentIdToIgnore) {
        AppointmentConfig config = appointmentConfigRepository.findByProfile_Id(profileId)
                .orElseThrow(() -> new BusinessException("Configuração de agendamento não encontrada."));

        LocalDateTime earliestBookingTime = LocalDateTime.now(BRASILIA_ZONE_ID).plusMinutes(config.getMinAdvanceNoticeMinutes());
        if (appointmentTime.isBefore(earliestBookingTime)) {
            throw new BusinessException("O agendamento deve ser feito com pelo menos " +
                    config.getMinAdvanceNoticeMinutes() + " minutos de antecedência.");
        }

        LocalDateTime appointmentEndTime = appointmentTime.plusMinutes(estimatedTime);

        List<CarWashAppointment> conflictingAppointments;

        if (appointmentIdToIgnore != null) {
            conflictingAppointments = appointmentRepository.findConflictingAppointmentsExcludingId(
                    profileId,
                    AppointmentStatus.CANCELED.name(),
                    appointmentTime,
                    appointmentEndTime,
                    appointmentIdToIgnore);
        } else {
            conflictingAppointments = appointmentRepository.findConflictingAppointments(
                    profileId,
                    AppointmentStatus.CANCELED.name(),
                    appointmentTime,
                    appointmentEndTime
            );
        }

        if (!conflictingAppointments.isEmpty()) {
            throw new BusinessException("Este horário já está ocupado ou em conflito. Por favor, escolha outro.");
        }
    }


    private void validateCoupon(Coupon coupon, UUID customerId, BigDecimal originalPrice) {
        LocalDateTime now = LocalDateTime.now(BRASILIA_ZONE_ID);
        if (coupon.getValidUntil().isBefore(now)) {
            throw new BusinessException("Este cupom está expirado.");
        }

        if (coupon.getValidFrom().isAfter(now)) {
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
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getUpcomingAppointmentsForCustomer(UUID customerId) {
        return appointmentRepository.findUpcomingByCustomerId(customerId)
                .stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
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

        if (appointment.getPaymentMethod() == PaymentMethod.PLATFORM) {
            appointment.setStatus(AppointmentStatus.REFUND_PENDING);
        } else {
            appointment.setStatus(AppointmentStatus.CANCELED);
        }

        appointment.setUpdatedAt(LocalDateTime.now(BRASILIA_ZONE_ID));

        CarWashAppointment updatedAppointment = appointmentRepository.save(appointment);

        notificationService.createNotificationForCancelledAppointment(updatedAppointment);

        return appointmentMapper.toResponse(updatedAppointment);
    }


    @Override
    @Transactional
    public AppointmentResponse completeAppointment(UUID appointmentId) {
        CarWashAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setUpdatedAt(LocalDateTime.now(BRASILIA_ZONE_ID));
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentResponse confirmRefund(UUID appointmentId, UUID profileId) {
        log.info("Tentando confirmar reembolso para o agendamento ID: {}", appointmentId);

        CarWashAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado com ID: " + appointmentId));

        if (!appointment.getProfile().getId().equals(profileId)) {
            throw new BusinessException("Este agendamento não pertence à sua empresa.");
        }

        if (appointment.getStatus() != AppointmentStatus.REFUND_PENDING) {
            throw new BusinessException("Ação não permitida. O status do agendamento não é 'Reembolso Pendente'.");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointment.setUpdatedAt(LocalDateTime.now(BRASILIA_ZONE_ID));

        CarWashAppointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Reembolso confirmado. Agendamento ID {} movido para o status CANCELED.", appointmentId);

        return appointmentMapper.toResponse(savedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse rescheduleAppointment(UUID appointmentId, UUID customerId, LocalDateTime newDateTime) {
        CarWashAppointment appointment = appointmentRepository.findByIdAndCustomerId(appointmentId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));

        validateActionDeadline(appointment, "edit");

        Integer totalDuration = appointment.getTotalDurationMinutes();

        if (totalDuration == null || totalDuration <= 0) {
            if (appointment.getSelectedServices() == null || appointment.getSelectedServices().isEmpty()) {
                throw new BusinessException("Não é possível reagendar um agendamento sem serviços associados.");
            }
            totalDuration = appointment.getSelectedServices().stream()
                    .mapToInt(service -> {
                        VehicleOfferingDetail detail = service.getVehicleDetails().get(appointment.getCarType());
                        return (detail != null && detail.getEstimatedTime() != null) ? detail.getEstimatedTime() : 0;
                    })
                    .sum();
        }

        if (totalDuration <= 0) {
            throw new BusinessException("A duração total dos serviços para este tipo de veículo é inválida. Não é possível reagendar.");
        }

        validateAppointmentTime(newDateTime, appointment.getProfile().getId(), totalDuration, appointmentId);

        appointment.setDateTime(newDateTime);

        appointment.setUpdatedAt(LocalDateTime.now(BRASILIA_ZONE_ID));

        CarWashAppointment updatedAppointment = appointmentRepository.save(appointment);

        notificationService.createNotificationForEditedAppointment(updatedAppointment);

        return appointmentMapper.toResponse(updatedAppointment);
    }

    private void validateActionDeadline(CarWashAppointment appointment, String action) {
        AppointmentConfig config = appointmentConfigRepository.findByProfile_Id(appointment.getProfile().getId())
                .orElseThrow(() -> new BusinessException("Configuração de agendamento não encontrada."));

        Integer noticeMinutes = action.equals("cancel") ? config.getMinCancelNoticeMinutes() : config.getMinEditNoticeMinutes();
        String actionText = action.equals("cancel") ? "cancelado" : "editado";

        if (noticeMinutes == null || noticeMinutes <= 0) return;

        LocalDateTime deadline = appointment.getDateTime().minusMinutes(noticeMinutes);

        if (LocalDateTime.now(BRASILIA_ZONE_ID).isAfter(deadline)) {
            throw new BusinessException("O prazo para esta ação expirou. Para " + actionText +
                    ", entre em contato com o profissional: " + appointment.getProfile().getCarWashRegistration().getPhone());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyAppointmentResponse> getUpcomingAppointmentsForCarWash(UUID profileId) {
        return appointmentRepository.findUpcomingByProfileId(profileId)
                .stream()
                .map(appointmentMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyAppointmentResponse> getCompletedAppointmentsForCarWash(UUID profileId) {
        return appointmentRepository.findByProfileIdAndStatus(profileId, AppointmentStatus.COMPLETED)
                .stream()
                .map(appointmentMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyAppointmentResponse> getCanceledAppointmentsForCarWash(UUID profileId) {
        return appointmentRepository.findByProfileIdAndStatus(profileId, AppointmentStatus.CANCELED)
                .stream()
                .map(appointmentMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyAppointmentResponse> getRefundPendingAppointmentsForCarWash(UUID profileId) {
        return appointmentRepository.findByProfileIdAndStatus(profileId, AppointmentStatus.REFUND_PENDING)
                .stream()
                .map(appointmentMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getRefundPendingCountForCarWash(UUID profileId) {
        long count = appointmentRepository.countByProfileIdAndStatus(profileId, AppointmentStatus.REFUND_PENDING);
        return Map.of("count", count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyAppointmentResponse> getAppointmentsByMonth(UUID profileId, int year, int month) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);

        return appointmentRepository.findByProfileIdAndDateRange(profileId, startDate, endDate)
                .stream()
                .map(appointmentMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentResponse createManualAppointment(UUID profileId, ManualAppointmentRequest request) {
        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil do Lava-rápido não encontrado com ID: " + profileId));

        ManualAppointmentRequest.CustomerPayload customerData = request.customer();
        CompanyCustomer companyCustomer;
        if (customerData.id() != null) {
            companyCustomer = companyCustomerRepository.findById(customerData.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente da empresa não encontrado com ID: " + customerData.id()));

            companyCustomer.setFullName(customerData.name());
            companyCustomer.setPhone(customerData.phone());
            companyCustomer.setZipCode(customerData.zipCode());
            companyCustomer.setStreet(customerData.street());
            companyCustomer.setStreetNumber(customerData.number());
            companyCustomer.setNeighborhood(customerData.neighborhood());
            companyCustomer.setCity(customerData.city());
            companyCustomer.setState(customerData.state());
            companyCustomer.setAdditionalInstructions(customerData.additionalInstructions());
        } else {
            companyCustomer = new CompanyCustomer();
            companyCustomer.setProfile(profile);
            companyCustomer.setFullName(customerData.name());
            companyCustomer.setPhone(customerData.phone());
            companyCustomer.setZipCode(customerData.zipCode());
            companyCustomer.setStreet(customerData.street());
            companyCustomer.setStreetNumber(customerData.number());
            companyCustomer.setNeighborhood(customerData.neighborhood());
            companyCustomer.setCity(customerData.city());
            companyCustomer.setState(customerData.state());
            companyCustomer.setAdditionalInstructions(customerData.additionalInstructions());
        }
        companyCustomer = companyCustomerRepository.save(companyCustomer);

        if (request.offeringIds() == null || request.offeringIds().isEmpty()) {
            throw new BusinessException("Pelo menos um serviço deve ser selecionado.");
        }
        List<CarWashOffering> selectedOfferings = offeringRepository.findAllById(request.offeringIds());
        if (selectedOfferings.isEmpty() || selectedOfferings.size() != request.offeringIds().size()) {
            throw new ResourceNotFoundException("Um ou mais serviços selecionados são inválidos.");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalTime = 0;
        String vehicleType = request.vehicleType();

        for (CarWashOffering service : selectedOfferings) {
            VehicleOfferingDetail detail = service.getVehicleDetails().get(vehicleType);
            if (detail != null && detail.getPrice() != null && detail.getEstimatedTime() != null) {
                totalPrice = totalPrice.add(detail.getPrice());
                totalTime += detail.getEstimatedTime();
            } else {
                throw new BusinessException("O serviço '" + service.getName() + "' não está disponível para o tipo de veículo '" + vehicleType + "'.");
            }
        }

        if (totalTime <= 0) {
            throw new BusinessException("A duração total dos serviços é inválida e não pode ser zero.");
        }

        validateAppointmentTime(request.startTime(), profile.getId(), totalTime);

        CustomerAddress address = getCustomerAddress(companyCustomer);
        address = addressRepository.save(address);

        CarWashAppointment appointment = new CarWashAppointment();
        appointment.setCustomer(null);
        appointment.setCompanyCustomer(companyCustomer);
        appointment.setProfile(profile);
        appointment.setAddress(address);
        appointment.setDateTime(request.startTime());
        appointment.setCarType(vehicleType);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setPaymentMethod(PaymentMethod.ON_SITE);
        appointment.setCreationChannel(CreationChannel.MANUAL);
        appointment.setOriginalAmount(totalPrice);
        appointment.setAmountPaid(totalPrice);
        appointment.setTotalDurationMinutes(totalTime);
        appointment.setSelectedServices(new HashSet<>(selectedOfferings));

        CarWashAppointment savedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.toResponse(savedAppointment);
    }

    private static CustomerAddress getCustomerAddress(CompanyCustomer companyCustomer) {
        CustomerAddress address = new CustomerAddress();
        address.setCustomer(null);
        address.setStreet(companyCustomer.getStreet());
        address.setStreetNumber(companyCustomer.getStreetNumber());
        address.setNeighborhood(companyCustomer.getNeighborhood());
        address.setCity(companyCustomer.getCity());
        address.setState(companyCustomer.getState());
        address.setZipCode(companyCustomer.getZipCode());
        address.setAdditionalInstructions(companyCustomer.getAdditionalInstructions());
        address.setDeletedAt(null);
        return address;
    }

    @Override
    @Transactional
    public AppointmentResponse rescheduleAppointmentByCompany(UUID profileId, UUID appointmentId, RescheduleByCompanyRequest request) {
        log.info("Iniciando reagendamento pela empresa para o agendamento ID {} do perfil {}", appointmentId, profileId);

        CarWashAppointment appointment = appointmentRepository.findByIdAndProfileId(appointmentId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado ou não pertence a esta empresa."));

        LocalDate newDate = LocalDate.parse(request.date());
        LocalTime newTime = LocalTime.parse(request.time());
        LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);

        Integer totalDuration = appointment.getTotalDurationMinutes();
        if (totalDuration == null || totalDuration <= 0) {
            throw new BusinessException("A duração do agendamento é inválida e não pode ser reagendado.");
        }
        validateAppointmentTime(newDateTime, profileId, totalDuration, appointmentId);

        appointment.setDateTime(newDateTime);
        appointment.setUpdatedAt(LocalDateTime.now(BRASILIA_ZONE_ID));

        CarWashAppointment updatedAppointment = appointmentRepository.save(appointment);
        log.info("Agendamento ID {} reagendado com sucesso para {}.", appointmentId, newDateTime);

        notificationService.createNotificationForEditedAppointment(updatedAppointment);

        return appointmentMapper.toResponse(updatedAppointment);
    }
}