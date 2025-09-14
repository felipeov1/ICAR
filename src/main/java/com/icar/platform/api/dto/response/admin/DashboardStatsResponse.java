package com.icar.platform.api.dto.response.admin;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class DashboardStatsResponse {
    private MrrStats mrr;
    private GmvStats gmv;
    private AppointmentStats appointments;
    private UserStats users;
    private FunnelStats funnel;
    private HistoricalStatsData historicalStats;
    private List<PartnerPerformanceDto> partnerPerformance;


    @Data
    public static class MrrStats {
        private BigDecimal total;
        private BigDecimal newThisMonth;
        private int activeSubscriptions;
        private BigDecimal changeFromLastMonth;
        private double changePercent;

    }

    @Data
    public static class GmvStats {
        private BigDecimal total;
        private BigDecimal marketplace;
        private BigDecimal manual;
        private double changePercent;
        private BigDecimal changeFromLastMonth; 

    }

    @Data
    public static class AppointmentStats {
        private StatValue today;
        private StatValue thisMonth;
        private Breakdown breakdown;

        @Data
        public static class StatValue {
            private int value;
            private double changePercent;
        }

        @Data
        public static class Breakdown {
            private int marketplace;
            private int manual;
        }
    }

    @Data
    public static class UserStats {
        private StatValue total;
        private StatValue newThisMonth;

        @Data
        public static class StatValue {
            private int value;
            private double changePercent;
        }
    }

    @Data
    public static class FunnelStats {
        private FunnelStage created;
        private FunnelStage pending;
        private FunnelStage completed;
        private FunnelStage canceled;
    }

    @Data
    public static class FunnelStage {
        private int total;
        private int marketplace;
        private int manual;
        private List<DetailItemDto> details;
    }

    @Data
    public static class DetailItemDto {
        private String id;
        private String customerName;
        private String contactInfo;
        private String partnerName;
        private String paymentStatus;
        private String date;
        private String creationChannel;
    }

    @Data
    public static class HistoricalStatsData {
        private HistoricalPeriod lastMonth;
        private HistoricalPeriod lastWeek;
        private HistoricalPeriod yesterday;
    }

    @Data
    public static class HistoricalPeriod {
        private int totalAppointments;
        private BigDecimal gmv;
        private int newUsers;
    }

    @Data
    public static class PartnerPerformanceDto {
        private UUID partnerId;
        private String partnerName;
        private int totalAppointments;
        private int marketplaceAppointments;
        private int manualAppointments;
    }

    @Data
    public static class PartnerMonthlyStatsDto {
        private String month;
        private int totalAppointments;
        private int marketplaceAppointments;
        private int manualAppointments;
    }
}
