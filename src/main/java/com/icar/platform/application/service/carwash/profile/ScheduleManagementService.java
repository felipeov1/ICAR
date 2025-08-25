package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.platform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.platform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.platform.api.dto.response.carwash.profile.TimeSlotResponse;
import com.icar.platform.api.dto.response.carwash.profile.WeeklyScheduleResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleManagementService {

    List<WeeklyScheduleResponse> getAllWeeklySchedules(UUID profileId);

    WeeklyScheduleResponse getWeeklySchedule(UUID profileId, UUID scheduleId);

    List<WeeklyScheduleResponse> createWeeklySchedules(UUID profileId, List<WeeklyScheduleRequest> requests);

    WeeklyScheduleResponse updateWeeklySchedule(UUID profileId, UUID scheduleId, WeeklyScheduleRequest request);

    void toggleWeeklyScheduleAvailability(UUID profileId, UUID scheduleId);

    List<WeeklyScheduleResponse> setAllWeeklySchedulesWithSameInterval(
            UUID profileId, LocalTime startTime, LocalTime endTime, int intervalMinutes, boolean available);

    List<SpecialDayResponse> getAllSpecialDays(UUID profileId);

    SpecialDayResponse createSpecialDay(UUID profileId, SpecialDayRequest request);

    SpecialDayResponse updateSpecialDay(UUID profileId, UUID specialDayId, SpecialDayRequest request);

    void deleteSpecialDay(UUID profileId, UUID specialDayId);

    List<TimeSlotResponse> getAvailableTimeSlots(UUID profileId, LocalDate date, Integer serviceDurationMinutes, UUID appointmentIdToIgnore);

    List<String> getAvailableDates(UUID profileId, LocalDate startDate, LocalDate endDate);
}