package com.icar.platform.api.dto.response.appointment;

import lombok.Data;
import com.icar.platform.domain.enums.AppointmentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class AppointmentResponse {
    private UUID id;
    private UUID carwashId;
    private UUID addressId;
    private String carWashName;
    private String carWashPhone;
    private List<ServiceInfo> services;
    private int totalDurationMinutes;
    private LocalDateTime dateTime;
    private String vehicleType;
    private AppointmentStatus status;
    private String paymentMethod;
    private BigDecimal finalPrice;
    private String addressName;
    private String addressStreet;
    private String addressNumber;
    private String addressNeighborhood;
    private String addressCityState;
    private String addressInstructions;
    private Integer minCancelNoticeMinutes;
    private Integer minEditNoticeMinutes;
    private boolean hasBeenReviewed;
    private BigDecimal originalPrice;
    private BigDecimal discountAmount;
    private BigDecimal convenienceFee;
    private CustomerInfo customer;

    @Data
    public static class CustomerInfo {
        private UUID id;
        private String name;
        private String phone;

        public CustomerInfo(UUID id, String name, String phone) {
            this.id = id;
            this.name = name;
            this.phone = phone;
        }
    }

    @Data
    public static class ServiceInfo {
        private UUID id;
        private String name;
        private BigDecimal price;
        private Integer time;
        private String serviceType;

        public ServiceInfo(UUID id, String name, BigDecimal price, Integer time, String serviceType) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.time = time;
            this.serviceType = serviceType;
        }
    }
}