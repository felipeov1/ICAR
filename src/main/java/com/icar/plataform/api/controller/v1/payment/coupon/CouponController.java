package com.icar.plataform.api.controller.v1.payment.coupon;

import com.icar.plataform.api.dto.request.payment.cupon.CouponCreateRequest;
import com.icar.plataform.api.dto.response.payment.coupon.CouponResponse;
import com.icar.plataform.application.service.payment.coupon.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Coupons Management", description = "Endpoint for coupon management")
@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "Create new coupon")
    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(
            @Valid @RequestBody CouponCreateRequest request) {
        return ResponseEntity.ok(couponService.createCoupon(request));
    }

    @Operation(summary = "Get all coupons for a car wash")
    @GetMapping("/carwash/{carWashId}")
    public ResponseEntity<List<CouponResponse>> getCarWashCoupons(
            @PathVariable UUID carWashId) {
        return ResponseEntity.ok(couponService.getCarWashCoupons(carWashId));
    }

    @Operation(summary = "Deactivate coupon")
    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deactivateCoupon(
            @PathVariable UUID couponId) {
        couponService.deactivateCoupon(couponId);
        return ResponseEntity.noContent().build();
    }
}