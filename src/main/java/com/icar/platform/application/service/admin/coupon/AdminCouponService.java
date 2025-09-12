package com.icar.platform.application.service.admin.coupon;

import com.icar.platform.api.dto.request.admin.CouponRequest;
import com.icar.platform.api.dto.response.admin.CouponResponse;
import com.icar.platform.api.dto.response.admin.PartnerSummaryResponse;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.coupon.Coupon;
import com.icar.platform.domain.repository.admin.AppliedCouponRepository;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.domain.repository.admin.CouponRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminCouponService {

    private final CouponRepository couponRepository;
    private final CarWashRegistrationDataRepository partnerRepository;
    private final AppliedCouponRepository appliedCouponRepository;

    @Transactional(readOnly = true)
    public Page<CouponResponse> findAllCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        if (couponRepository.existsByCode(request.code())) {
            throw new BusinessException("Um cupom com este código já existe.");
        }
        Coupon coupon = new Coupon();
        updateCouponFromRequest(coupon, request);
        Coupon savedCoupon = couponRepository.save(coupon);
        return toResponse(savedCoupon);
    }

    private void updateCouponFromRequest(Coupon coupon, CouponRequest request) {
        coupon.setCode(request.code());
        coupon.setValidFrom(request.validFrom());
        coupon.setValidUntil(request.validUntil());
        coupon.setMaxUses(request.maxUses());
        coupon.setMaxUsesPerUser(request.maxUsesPerUser());
        coupon.setMinOrderValue(request.minOrderValue());

        BigDecimal discountValue = request.discountValue();
        BigDecimal discountPercentage = request.discountPercentage();

        if (discountValue != null && discountValue.compareTo(BigDecimal.ZERO) > 0 &&
                discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("Forneça apenas um tipo de desconto: valor fixo ou percentual.");
        }

        if (discountValue != null && discountValue.compareTo(BigDecimal.ZERO) > 0) {
            coupon.setDiscountValue(discountValue);
            coupon.setDiscountPercentage(null);
        } else if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            coupon.setDiscountPercentage(discountPercentage);
            coupon.setDiscountValue(null);
        } else {
            throw new BusinessException("É necessário especificar um valor de desconto (fixo ou percentual).");
        }

        if (request.profileId() != null) {
            CarWashRegistration partner = partnerRepository.findById(request.profileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parceiro não encontrado."));

            CarWashProfile profile = partner.getProfile();
            if (profile == null) {
                throw new BusinessException("O parceiro selecionado não possui um perfil de lava-rápido associado.");
            }
            coupon.setProfile(profile);

        } else {
            coupon.setProfile(null);
        }
    }

    @Transactional
    public CouponResponse updateCoupon(UUID id, CouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado."));

        if (!coupon.getCode().equals(request.code()) && couponRepository.existsByCode(request.code())) {
            throw new BusinessException("Um cupom com este código já existe.");
        }

        updateCouponFromRequest(coupon, request);

        Coupon updatedCoupon = couponRepository.save(coupon);

        return toResponse(updatedCoupon);
    }

    @Transactional
    public void deleteCoupon(UUID id) {
        if (!couponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cupom não encontrado.");
        }
        couponRepository.deleteById(id);
    }


    private CouponResponse toResponse(Coupon coupon) {
        PartnerSummaryResponse partnerSummary = null;
        if (coupon.getProfile() != null) {
            partnerSummary = new PartnerSummaryResponse(coupon.getProfile().getId(), coupon.getProfile().getName());
        }

        int completedUses = appliedCouponRepository.countByCouponIdAndAppointmentStatus(coupon.getId(), AppointmentStatus.COMPLETED);

        return new CouponResponse(
                coupon.getId(),
                coupon.getCode(),
                partnerSummary,
                coupon.getDiscountValue(),
                coupon.getDiscountPercentage(),
                coupon.getValidFrom(),
                coupon.getValidUntil(),
                coupon.getMaxUses(),
                coupon.getMaxUsesPerUser(),
                completedUses,
                coupon.getCreatedAt()
        );
    }
}