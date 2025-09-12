package com.icar.platform.api.controller.v1.admin.dashboard;

import com.icar.platform.api.dto.response.admin.DashboardStatsResponse;
import com.icar.platform.application.service.admin.dashboard.AdminDashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
