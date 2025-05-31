package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.plataform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.plataform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.plataform.api.dto.response.carwash.profile.TimeSlotResponse;
import com.icar.plataform.api.dto.response.carwash.profile.WeeklyScheduleResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleManagementService {
    // Weekly schedules
    List<WeeklyScheduleResponse> getAllWeeklySchedules(UUID carWashId);
    WeeklyScheduleResponse getWeeklySchedule(UUID carWashId, UUID scheduleId);
    List<WeeklyScheduleResponse> createWeeklySchedules(UUID carWashId, List<WeeklyScheduleRequest> requests);
    WeeklyScheduleResponse updateWeeklySchedule(UUID carWashId, UUID scheduleId, WeeklyScheduleRequest request);
    void toggleWeeklyScheduleAvailability(UUID carWashId, UUID scheduleId);
    List<WeeklyScheduleResponse> setAllWeeklySchedulesWithSameInterval(
            UUID carWashId,
            LocalTime startTime,
            LocalTime endTime,
            int intervalMinutes,
            boolean available);

    // Special days
    List<SpecialDayResponse> getAllSpecialDays(UUID carWashId, boolean activeOnly);
    List<SpecialDayResponse> getSpecialDaysByDateRange(UUID carWashId, LocalDate startDate, LocalDate endDate, boolean activeOnly);
    SpecialDayResponse getSpecialDay(UUID carWashId, UUID specialDayId, boolean includeInactive);
    List<SpecialDayResponse> createSpecialDays(UUID carWashId, List<SpecialDayRequest> requests);
    SpecialDayResponse updateSpecialDay(UUID carWashId, UUID specialDayId, SpecialDayRequest request);
    void deleteSpecialDay(UUID carWashId, UUID specialDayId);
    void restoreSpecialDay(UUID carWashId, UUID specialDayId);

    // Availability
    List<TimeSlotResponse> getAvailableTimeSlots(UUID carWashId, LocalDate date);
}