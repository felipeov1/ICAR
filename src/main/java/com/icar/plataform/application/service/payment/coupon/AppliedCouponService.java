package com.icar.plataform.application.service.payment.coupon;

import com.icar.plataform.api.dto.response.payment.coupon.AppliedCouponResponse;
import com.icar.plataform.domain.model.payment.coupon.AppliedCoupon;
import com.icar.plataform.domain.repository.payment.coupon.AppliedCouponRepository;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppliedCouponService {

    private final AppliedCouponRepository appliedCouponRepository;

    @Transactional(readOnly = true)
    public AppliedCouponResponse getById(UUID id) {
        AppliedCoupon appliedCoupon = appliedCouponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom aplicado não encontrado"));
        return toResponse(appliedCoupon);
    }

    @Transactional(readOnly = true)
    public List<AppliedCouponResponse> getByAppointment(UUID appointmentId) {
        return appliedCouponRepository.findByAppointmentId(appointmentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppliedCouponResponse> getCustomerHistory(UUID customerId) {
        return appliedCouponRepository.findHistoryByCustomer(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAppliedCoupon(UUID id) {
        if (!appliedCouponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cupom aplicado não encontrado");
        }
        appliedCouponRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByAppointment(UUID appointmentId) {
        appliedCouponRepository.deleteAllByAppointmentId(appointmentId);
    }

    @Transactional(readOnly = true)
    public int countUsesByCoupon(UUID couponId) {
        return appliedCouponRepository.countByCouponId(couponId);
    }

    @Transactional(readOnly = true)
    public int countUsesByCustomerAndCoupon(UUID customerId, UUID couponId) {
        return appliedCouponRepository.countByCouponAndCustomer(couponId, customerId);
    }

    @Transactional(readOnly = true)
    public boolean isCouponAppliedToAppointment(UUID appointmentId, UUID couponId) {
        return appliedCouponRepository.existsByAppointmentIdAndCouponId(appointmentId, couponId);
    }

    private AppliedCouponResponse toResponse(AppliedCoupon appliedCoupon) {
        return new AppliedCouponResponse(
                appliedCoupon.getId(),
                appliedCoupon.getCoupon().getId(),
                appliedCoupon.getCoupon().getCode(),
                appliedCoupon.getAppointment().getId(),
                appliedCoupon.getCustomer().getId(),
                appliedCoupon.getOriginalAmount(),
                appliedCoupon.getFinalAmount(),
                appliedCoupon.getDiscountApplied(),
                appliedCoupon.getAppliedAt()
        );
    }
}