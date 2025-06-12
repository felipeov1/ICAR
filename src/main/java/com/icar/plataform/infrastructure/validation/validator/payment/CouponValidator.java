package com.icar.plataform.infrastructure.validation.validator.payment;

import com.icar.plataform.domain.model.payment.coupon.Coupon;
import com.icar.plataform.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CouponValidator {

    public void validateCoupon(Coupon coupon, UUID customerId, UUID profileId, BigDecimal orderValue) {
        LocalDateTime now = LocalDateTime.now();

        if (coupon.getValidFrom().isAfter(now)) {
            throw new BusinessException("Este cupom ainda não está válido");
        }

        if (coupon.getValidUntil().isBefore(now)) {
            throw new BusinessException("Este cupom expirou");
        }

        if (coupon.getMaxUses() != null && coupon.getCurrentUses() >= coupon.getMaxUses()) {
            throw new BusinessException("Limite de usos deste cupom atingido");
        }

        if (!coupon.getProfile().getId().equals(profileId)) {
            throw new BusinessException("Este cupom não é válido para este lava-rápido");
        }

        if (coupon.getMinOrderValue() != null && orderValue.compareTo(coupon.getMinOrderValue()) < 0) {
            throw new BusinessException(String.format(
                    "Este cupom requer um valor mínimo de pedido de %.2f",
                    coupon.getMinOrderValue()));
        }
    }
}