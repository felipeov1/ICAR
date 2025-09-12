package com.icar.platform.api.dto.response.admin;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardStatsResponse {
    private MrrStats mrr;
    private GmvStats gmv;
    private AppointmentStats appointments;
    private UserStats users;
    private FunnelStats funnel;
    private HistoricalStatsData historicalStats;

    @Data
    public static class MrrStats {
        private BigDecimal total;
        private BigDecimal newThisMonth;
        private int activeSubscriptions;
        private BigDecimal changeFromLastMonth;
    }

    @Data
    public static class GmvStats {
        private BigDecimal total;
        private BigDecimal marketplace;
        private BigDecimal manual;
        private double changePercent;
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
}