package com.icar.platform.api.controller.v1.payment.coupon;

import com.icar.platform.api.dto.response.payment.coupon.AppliedCouponResponse;
import com.icar.platform.application.service.payment.coupon.AppliedCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Applied Coupons", description = "Gerenciamento de cupons aplicados")
@RestController
@RequestMapping("/api/v1/applied-coupons")
@RequiredArgsConstructor
public class AppliedCouponController {

    private final AppliedCouponService appliedCouponService;

    @Operation(summary = "Obter cupom aplicado por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AppliedCouponResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(appliedCouponService.getById(id));
    }

    @Operation(summary = "Listar cupons aplicados a um agendamento")
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<AppliedCouponResponse>> getByAppointment(
            @PathVariable UUID appointmentId) {
        return ResponseEntity.ok(appliedCouponService.getByAppointment(appointmentId));
    }

    @Operation(summary = "Histórico de cupons usados por cliente")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AppliedCouponResponse>> getCustomerHistory(
            @PathVariable UUID customerId) {
        return ResponseEntity.ok(appliedCouponService.getCustomerHistory(customerId));
    }

    @Operation(summary = "Contar usos de um cupom")
    @GetMapping("/count/coupon/{couponId}")
    public ResponseEntity<Integer> countUsesByCoupon(
            @PathVariable UUID couponId) {
        return ResponseEntity.ok(appliedCouponService.countUsesByCoupon(couponId));
    }

    @Operation(summary = "Contar usos de um cupom por cliente")
    @GetMapping("/count/coupon/{couponId}/customer/{customerId}")
    public ResponseEntity<Integer> countUsesByCustomerAndCoupon(
            @PathVariable UUID couponId,
            @PathVariable UUID customerId) {
        return ResponseEntity.ok
                (Math.toIntExact(appliedCouponService.countUsesByCustomerAndCoupon(customerId, couponId)));
    }

    @Operation(summary = "Verificar se cupom foi aplicado a um agendamento")
    @GetMapping("/exists/appointment/{appointmentId}/coupon/{couponId}")
    public ResponseEntity<Boolean> isCouponAppliedToAppointment(
            @PathVariable UUID appointmentId,
            @PathVariable UUID couponId) {
        return ResponseEntity.ok(appliedCouponService.isCouponAppliedToAppointment(appointmentId, couponId));
    }

    @Operation(summary = "Remover cupom aplicado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppliedCoupon(
            @PathVariable UUID id) {
        appliedCouponService.deleteAppliedCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remover todos cupons de um agendamento")
    @DeleteMapping("/appointment/{appointmentId}")
    public ResponseEntity<Void> deleteAllByAppointment(
            @PathVariable UUID appointmentId) {
        appliedCouponService.deleteAllByAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }
}