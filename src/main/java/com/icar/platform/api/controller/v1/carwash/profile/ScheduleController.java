package com.icar.platform.api.controller.v1.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.platform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.platform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.platform.api.dto.response.carwash.profile.TimeSlotResponse;
import com.icar.platform.api.dto.response.carwash.profile.WeeklyScheduleResponse;
import com.icar.platform.application.service.carwash.profile.ScheduleManagementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile/{profileId}/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleManagementService scheduleService;

    @Operation(summary = "Get all weekly schedules", tags = {"Car Wash Profile - Schedule - Weekly"})
    @GetMapping("/weekly")
    public ResponseEntity<List<WeeklyScheduleResponse>> getAllWeeklySchedules(
            @PathVariable UUID profileId) {
        List<WeeklyScheduleResponse> schedules = scheduleService.getAllWeeklySchedules(profileId);
        return ResponseEntity.ok(schedules);
    }


    @Operation(summary = "Update weekly schedule", tags = {"Car Wash Profile - Schedule - Weekly"})
    @PutMapping("/weekly/{scheduleId}")
    public ResponseEntity<WeeklyScheduleResponse> updateWeeklySchedule(
            @PathVariable UUID profileId,
            @PathVariable UUID scheduleId,
            @Valid @RequestBody WeeklyScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.updateWeeklySchedule(profileId, scheduleId, request));
    }

    @Operation(summary = "Get all upcoming special days for a profile", tags = {"Profile Schedule"})
    @GetMapping("/special-days")
    public ResponseEntity<List<SpecialDayResponse>> getSpecialDays(@PathVariable UUID profileId) {
        return ResponseEntity.ok(scheduleService.getAllSpecialDays(profileId));
    }

    @Operation(summary = "Create a special day or period for a profile", tags = {"Profile Schedule"})
    @PostMapping("/special-days")
    public ResponseEntity<SpecialDayResponse> createSpecialDay(
            @PathVariable UUID profileId,
            @Valid @RequestBody SpecialDayRequest request) {
        return ResponseEntity.ok(scheduleService.createSpecialDay(profileId, request));
    }

    @Operation(summary = "Update a special day or period for a profile", tags = {"Profile Schedule"})
    @PutMapping("/special-days/{specialDayId}")
    public ResponseEntity<SpecialDayResponse> updateSpecialDay(
            @PathVariable UUID profileId,
            @PathVariable UUID specialDayId,
            @Valid @RequestBody SpecialDayRequest request) {
        return ResponseEntity.ok(scheduleService.updateSpecialDay(profileId, specialDayId, request));
    }

    @Operation(summary = "Deactivate (soft delete) a special day or period", tags = {"Profile Schedule"})
    @DeleteMapping("/special-days/{specialDayId}")
    public ResponseEntity<Void> deleteSpecialDay(
            @PathVariable UUID profileId,
            @PathVariable UUID specialDayId) {
        scheduleService.deleteSpecialDay(profileId, specialDayId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/availability")
    public ResponseEntity<List<TimeSlotResponse>> getAvailableTimeSlots(
            @PathVariable UUID profileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Integer serviceDurationMinutes,
            @RequestParam(required = false) UUID appointmentIdToIgnore,
            @RequestParam(required = false, defaultValue = "false") boolean isAdminContext) {
        return ResponseEntity.ok(scheduleService.getAvailableTimeSlots(profileId, date, serviceDurationMinutes, appointmentIdToIgnore, isAdminContext));
    }

    @GetMapping("/available-dates")
    public ResponseEntity<List<String>> getAvailableDates(
            @PathVariable UUID profileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "false") boolean isAdminContext) {
        return ResponseEntity.ok(scheduleService.getAvailableDates(profileId, startDate, endDate, isAdminContext));
    }
}