package com.icar.platform.api.controller.v1.admin.coupon;

import com.icar.platform.api.dto.response.admin.CouponRecentUsageResponse;
import com.icar.platform.api.dto.response.admin.CouponReportResponse;
import com.icar.platform.application.service.admin.coupon.AdminReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
@Tag(name = "Admin: Reports")
public class AdminReportController {

    private final AdminReportService adminReportService;

    @GetMapping("/coupon-usage/recent")
    public ResponseEntity<List<CouponRecentUsageResponse>> getRecentCouponUsages() {
        return ResponseEntity.ok(adminReportService.getRecentCouponUsages());
    }

    @GetMapping("/coupons")
    public ResponseEntity<CouponReportResponse> generateCouponReport(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("partnerId") String partnerId
    ) {
        return ResponseEntity.ok(adminReportService.generateCouponReport(date, partnerId));
    }
}