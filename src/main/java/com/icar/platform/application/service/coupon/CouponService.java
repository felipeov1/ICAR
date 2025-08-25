package com.icar.platform.application.service.coupon;

import com.icar.platform.api.dto.request.cupon.CouponCreateRequest;
import com.icar.platform.api.dto.response.coupon.CouponResponse;
import com.icar.platform.api.dto.response.coupon.CouponValidationResponse;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.coupon.Coupon;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.domain.repository.coupon.AppliedCouponRepository;
import com.icar.platform.domain.repository.coupon.CouponRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final AppliedCouponRepository appliedCouponRepository;
    private final CarWashProfileRepository carWashProfileRepository;

    @Transactional
    public CouponResponse createCoupon(CouponCreateRequest request) {
        if (couponRepository.existsByCode(request.code())) {
            throw new BusinessException("Já existe um cupom com este código");
        }

        Coupon coupon = new Coupon();
        coupon.setCode(request.code());
        coupon.setDiscountValue(request.discountValue());
        coupon.setDiscountPercentage(request.discountPercentage());
        coupon.setValidFrom(LocalDateTime.from(request.validFrom()));
        coupon.setValidUntil(LocalDateTime.from(request.validUntil()));
        coupon.setMaxUses(request.maxUses());
        coupon.setMaxUsesPerUser(request.maxUsesPerUser());
        coupon.setMinOrderValue(request.minOrderValue());
        coupon.setCurrentUses(0);

        if (request.profileId() != null) {
            CarWashProfile profile = carWashProfileRepository.findById(request.profileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lava-rápido não encontrado para associar ao cupom."));
            coupon.setProfile(profile);
        } else {
            coupon.setProfile(null);
        }

        Coupon savedCoupon = couponRepository.save(coupon);
        return toResponse(savedCoupon);
    }


    @Transactional(readOnly = true)
    public CouponValidationResponse validateCouponForUse(String code, UUID carwashId, UUID customerId, BigDecimal orderValue) {
        Coupon coupon = couponRepository.findValidByCodeAndProfile(code, carwashId)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom inválido ou não encontrado."));

        if (coupon.getValidFrom().isAfter(LocalDateTime.now())) {
            throw new BusinessException("Este cupom ainda não está válido.");
        }
        if (coupon.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Este cupom expirou.");
        }
        if (coupon.getMaxUses() != null && coupon.getCurrentUses() >= coupon.getMaxUses()) {
            throw new BusinessException("Este cupom atingiu o limite máximo de usos.");
        }
        if (coupon.getMinOrderValue() != null && orderValue.compareTo(coupon.getMinOrderValue()) < 0) {
            throw new BusinessException(String.format("O pedido não atinge o valor mínimo de R$ %.2f.", coupon.getMinOrderValue()));
        }
        if (coupon.getMaxUsesPerUser() != null) {
            long usesByCustomer = appliedCouponRepository.countByCouponIdAndCustomerId(coupon.getId(), customerId);
            if (usesByCustomer >= coupon.getMaxUsesPerUser()) {
                throw new BusinessException("Você já utilizou o limite máximo para este cupom.");
            }
        }

        String discountType = coupon.getDiscountPercentage() != null ? "PERCENTAGE" : "FIXED";
        BigDecimal discountValue = coupon.getDiscountPercentage() != null ? coupon.getDiscountPercentage() : coupon.getDiscountValue();

        if (discountValue == null) {
            throw new BusinessException("Formato de cupom inválido no sistema.");
        }

        return new CouponValidationResponse(coupon.getCode(), discountType, discountValue);
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> getCarWashCoupons(UUID carWashId) {
        return couponRepository.findByProfileId(carWashId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deactivateCoupon(UUID couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));
        coupon.setValidUntil(LocalDateTime.now());
        couponRepository.save(coupon);
    }

    private CouponResponse toResponse(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getProfile() != null ? coupon.getProfile().getId() : null,
                coupon.getDiscountValue(),
                coupon.getDiscountPercentage(),
                coupon.getValidFrom(),
                coupon.getValidUntil(),
                coupon.getMaxUses(),
                coupon.getMaxUsesPerUser(),
                coupon.getCurrentUses(),
                coupon.getMinOrderValue()
        );
    }
}