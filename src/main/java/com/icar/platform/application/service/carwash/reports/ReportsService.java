package com.icar.platform.application.service.carwash.reports;

import com.icar.platform.api.dto.response.carwash.reports.ReportDataResponse;
import java.util.UUID;

public interface ReportsService {
    ReportDataResponse generateReport(UUID profileId, String periodStr);
}