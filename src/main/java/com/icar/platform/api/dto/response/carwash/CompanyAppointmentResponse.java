package com.icar.platform.api.dto.response.carwash;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAppointmentResponse {
    private UUID id;
    private LocalDateTime dateTime;
    private String status;
    private BigDecimal finalPrice;
    private int totalDurationMinutes;
    private String vehicleType;
    private String paymentMethod;
    private CustomerInfo customer;
    private List<ServiceInfo> services;
    private String addressStreet;
    private String addressNumber;
    private String addressCityState;
    private BigDecimal originalPrice;
    private BigDecimal discountAmount;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerInfo {
        private UUID id;
        private String name;
        private String phone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceInfo {
        private UUID id;
        private String name;
        private BigDecimal price;
        private Integer time;
    }
}