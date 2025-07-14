package com.icar.platform.api.dto.response.appointment;

import com.icar.platform.domain.enums.AppointmentStatus;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        UUID carwashId,
        UUID offeringId,
        UUID addressId,
        String carWashName,
        String carWashPhone,
        String serviceName,
        List<ExtraServiceInfo> extraServices,
        int estimatedTime,
        ZonedDateTime dateTime,
        String vehicleType,
        AppointmentStatus status,
        String paymentMethod,
        BigDecimal finalPrice,
        String addressStreet,
        String addressNumber,
        String addressCityState,
        String addressInstructions,
        Integer minCancelNoticeMinutes,
        Integer minEditNoticeMinutes,
        boolean hasBeenReviewed
) {
    public record ExtraServiceInfo(UUID id, String name, BigDecimal price, Integer integer) {}
}