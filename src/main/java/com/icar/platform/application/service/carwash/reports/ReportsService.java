package com.icar.platform.application.service.carwash.reports;

import com.icar.platform.api.dto.response.carwash.reports.ReportDataResponse;
import java.util.UUID;

public interface ReportsService {

    /**
     * Generates a performance report for a car wash profile based on a given period.
     * @param profileId The ID of the car wash profile.
     * @param periodStr A string representing the period ("WEEK", "MONTH", "YEAR").
     * @return A DTO containing all the calculated report data.
     */
    ReportDataResponse generateReport(UUID profileId, String periodStr);
}