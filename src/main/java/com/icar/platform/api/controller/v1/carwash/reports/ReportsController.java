package com.icar.platform.api.controller.v1.carwash.reports;

import com.icar.platform.api.dto.response.carwash.reports.ReportDataResponse;
import com.icar.platform.application.service.carwash.reports.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Car Wash - Reports", description = "Endpoints for car wash performance reports")
@RestController
@RequestMapping("/api/v1/profile/{profileId}/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final ReportsService reportsService;

    @Operation(summary = "Get car wash performance report data by period")
    @GetMapping
    public ResponseEntity<ReportDataResponse> getReportData(
            @Parameter(description = "ID of the Car Wash Profile")
            @PathVariable UUID profileId,

            @Parameter(description = "The reporting period. Can be 'WEEK', 'MONTH', or 'YEAR'. Defaults to 'WEEK'.")
            @RequestParam(defaultValue = "WEEK") String period) {

        ReportDataResponse reportData = reportsService.generateReport(profileId, period);
        return ResponseEntity.ok(reportData);
    }
}