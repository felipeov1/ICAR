package com.icar.platform.api.controller.v1.admin.coupon;

import com.icar.platform.api.dto.request.admin.CouponRequest;
import com.icar.platform.api.dto.response.admin.CouponResponse;
import com.icar.platform.application.service.admin.coupon.AdminCouponService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/coupons")
@RequiredArgsConstructor
@Tag(name = "Admin: Coupon Management")
public class AdminCouponController {

    private final AdminCouponService adminCouponService;

    @GetMapping
    public ResponseEntity<Page<CouponResponse>> getAllCoupons(Pageable pageable) {
        return ResponseEntity.ok(adminCouponService.findAllCoupons(pageable));
    }

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest request) {
        return new ResponseEntity<>(adminCouponService.createCoupon(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponse> updateCoupon(@PathVariable UUID id, @RequestBody CouponRequest request) {
        return ResponseEntity.ok(adminCouponService.updateCoupon(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable UUID id) {
        adminCouponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }
}