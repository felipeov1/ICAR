package com.icar.plataform.application.service.payment.coupon;

import com.icar.plataform.api.dto.request.payment.cupon.CouponCreateRequest;
import com.icar.plataform.api.dto.response.payment.coupon.CouponResponse;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.model.payment.coupon.Coupon;
import com.icar.plataform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.plataform.domain.repository.payment.coupon.CouponRepository;
import com.icar.plataform.shared.exception.BusinessException;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CarWashProfileRepository carWashProfileRepository;

    @Transactional
    public CouponResponse createCoupon(CouponCreateRequest request) {
        CarWashProfile profile = carWashProfileRepository.findById(request.profileId())
                .orElseThrow(() -> new ResourceNotFoundException("Lava-rápido não encontrado"));

        // Verificar se código já existe
        if (couponRepository.existsByCode(request.code())) {
            throw new BusinessException("Já existe um cupom com este código");
        }

        Coupon coupon = getCoupon(request, profile);

        Coupon savedCoupon = couponRepository.save(coupon);
        return toResponse(savedCoupon);
    }

    private static Coupon getCoupon(CouponCreateRequest request, CarWashProfile profile) {
        Coupon coupon = new Coupon();
        coupon.setCode(request.code());
        coupon.setProfile(profile);
        coupon.setDiscountValue(request.discountValue());
        coupon.setDiscountPercentage(request.discountPercentage());
        coupon.setValidFrom(request.validFrom());
        coupon.setValidUntil(request.validUntil());
        coupon.setMaxUses(request.maxUses());
        coupon.setMaxUsesPerUser(request.maxUsesPerUser());
        coupon.setMinOrderValue(request.minOrderValue());
        coupon.setCurrentUses(0);
        return coupon;
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
                coupon.getProfile().getId(),
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