package com.icar.platform.application.service.coupon;

import com.icar.platform.api.dto.response.coupon.AppliedCouponResponse;
import com.icar.platform.domain.model.coupon.AppliedCoupon;
import com.icar.platform.domain.repository.admin.AppliedCouponRepository;
import com.icar.platform.shared.exception.ResourceNotFoundException;
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
        return appliedCouponRepository.findHistoryByCustomerId(customerId).stream()
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
    public long countUsesByCustomerAndCoupon(UUID customerId, UUID couponId) {
        return appliedCouponRepository.countByCouponIdAndCustomerId(couponId, customerId);
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