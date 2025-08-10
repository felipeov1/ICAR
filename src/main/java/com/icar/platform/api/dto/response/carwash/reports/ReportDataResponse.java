package com.icar.platform.api.dto.response.carwash.reports;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ReportDataResponse {
    private BigDecimal totalRevenue;
    private BigDecimal revenueOnline;
    private BigDecimal revenueLocal;
    private ServicesCompleted servicesCompleted;
    private int servicesChange;
    private BigDecimal averageRating;
    private int totalReviews;
    private List<ServiceCountDTO> topServices;
    private List<SimpleReviewDTO> recentReviews;

    @Data
    @Builder
    public static class ServicesCompleted {
        private int total;
        private int marketplace;
        private int manual;
    }
}