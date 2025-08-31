package com.icar.platform.api.dto.request.carwash.onboarding;

import java.util.List;
import java.util.Map;

public record OnboardingRequest(
        ProfilePart profile,
        OperationPart operation,
        SchedulePart schedule,
        List<ServicePart> services
) {
    public record ProfilePart(String name, String whatsapp, String description) {}
    public record OperationPart(List<String> locations, List<String> modalities, List<String> vehicleTypes, String observations) {}
    public record SchedulePart(Map<String, DaySchedulePart> week, AppointmentRulesPart appointmentRules) {}
    public record DaySchedulePart(boolean available, String startTime, String endTime) {}
    public record AppointmentRulesPart(Integer minAdvanceNoticeMinutes, Integer maxAdvanceBookingDays, Integer minEditNoticeMinutes, Integer minCancelNoticeMinutes, boolean allowActionsAtAnyTime, boolean allowOvertime, Integer gapMinutes) {}
    public record ServicePart(String name, String description, String serviceType, Map<String, VehicleDetailPart> vehicleDetails) {}
    public record VehicleDetailPart(java.math.BigDecimal price, Integer durationMinutes) {}
}