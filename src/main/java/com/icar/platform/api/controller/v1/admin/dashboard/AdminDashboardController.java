package com.icar.platform.api.controller.v1.admin.dashboard;

import com.icar.platform.api.dto.response.admin.DashboardStatsResponse;
import com.icar.platform.application.service.admin.dashboard.AdminDashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "Admin: Dashboard")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(
            @RequestParam(defaultValue = "month") String period) {
        DashboardStatsResponse stats = dashboardService.getDashboardStats(period);
        return ResponseEntity.ok(stats);
    }
    @GetMapping("/partners/{partnerId}/monthly-stats")
    public ResponseEntity<List<DashboardStatsResponse.PartnerMonthlyStatsDto>> getPartnerMonthlyStats(@PathVariable UUID partnerId) {
        List<DashboardStatsResponse.PartnerMonthlyStatsDto> stats = dashboardService.getPartnerMonthlyStats(partnerId);
        return ResponseEntity.ok(stats);
    }
}
