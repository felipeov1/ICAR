package com.icar.platform.infrastructure.validation.validator.payment;

import com.icar.platform.domain.model.payment.coupon.Coupon;
import com.icar.platform.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class CouponValidator {

    public void validateCoupon(Coupon coupon, UUID customerId, BigDecimal orderValue) {
        ZonedDateTime now = ZonedDateTime.now();

        if (coupon.getValidFrom().isAfter(now)) {
            throw new BusinessException("Este cupom ainda não está válido");
        }

        if (coupon.getValidUntil().isBefore(now)) {
            throw new BusinessException("Este cupom expirou");
        }

        if (coupon.getMaxUses() != null && coupon.getCurrentUses() >= coupon.getMaxUses()) {
            throw new BusinessException("Limite de usos deste cupom atingido");
        }

        if (coupon.getMinOrderValue() != null && orderValue.compareTo(coupon.getMinOrderValue()) < 0) {
            throw new BusinessException(String.format(
                    "Este cupom requer um valor mínimo de pedido de %.2f",
                    coupon.getMinOrderValue()));
        }
    }
}