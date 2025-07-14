package com.icar.platform.api.dto.response.payment.coupon;

import java.math.BigDecimal;

public record CouponValidationResponse(
        String code,
        String discountType,
        BigDecimal discountValue
) {}