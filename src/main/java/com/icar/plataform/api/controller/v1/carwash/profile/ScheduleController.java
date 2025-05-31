package com.icar.plataform.api.controller.v1.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.plataform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.plataform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.plataform.api.dto.response.carwash.profile.TimeSlotResponse;
import com.icar.plataform.api.dto.response.carwash.profile.WeeklyScheduleResponse;
import com.icar.plataform.application.service.carwash.profile.ScheduleManagementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carwashes/{carwashId}/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleManagementService scheduleService;

    // ===== WEEKLY SCHEDULES =====
    @Operation(summary = "Get all weekly schedules", tags = {"Car Wash Profile - Schedule - Weekly"})
    @GetMapping("/weekly")
    public ResponseEntity<List<WeeklyScheduleResponse>> getAllWeeklySchedules(
            @PathVariable UUID carwashId) {
        List<WeeklyScheduleResponse> schedules = scheduleService.getAllWeeklySchedules(carwashId);
        return ResponseEntity.ok(schedules);
    }

    @Operation(summary = "Get a specific weekly schedule", tags = {"Car Wash Profile - Schedule - Weekly"})
    @GetMapping("/weekly/{scheduleId}")
    public ResponseEntity<WeeklyScheduleResponse> getWeeklySchedule(
            @PathVariable UUID carwashId,
            @PathVariable UUID scheduleId) {
        return ResponseEntity.ok(scheduleService.getWeeklySchedule(carwashId, scheduleId));
    }

    @Operation(summary = "Create weekly schedules", tags = {"Car Wash Profile - Schedule - Weekly"})
    @PostMapping("/weekly")
    public ResponseEntity<List<WeeklyScheduleResponse>> createWeeklySchedules(
            @PathVariable UUID carwashId,
            @Valid @RequestBody List<WeeklyScheduleRequest> requests) {
        return ResponseEntity.ok(scheduleService.createWeeklySchedules(carwashId, requests));
    }

    @Operation(summary = "Set same schedule for all days", tags = {"Car Wash Profile - Schedule - Weekly"})
    @PostMapping("/weekly/set-all")
    public ResponseEntity<List<WeeklyScheduleResponse>> setAllWeeklySchedules(
            @PathVariable UUID carwashId,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
            @RequestParam int intervalMinutes,
            @RequestParam(defaultValue = "true") boolean available) {
        return ResponseEntity.ok(scheduleService.setAllWeeklySchedulesWithSameInterval(
                carwashId, startTime, endTime, intervalMinutes, available
        ));
    }

    @Operation(summary = "Update weekly schedule", tags = {"Car Wash Profile - Schedule - Weekly"})
    @PutMapping("/weekly/{scheduleId}")
    public ResponseEntity<WeeklyScheduleResponse> updateWeeklySchedule(
            @PathVariable UUID carwashId,
            @PathVariable UUID scheduleId,
            @Valid @RequestBody WeeklyScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.updateWeeklySchedule(carwashId, scheduleId, request));
    }

    @Operation(summary = "Toggle availability for weekly schedule", tags = {"Car Wash Profile - Schedule - Weekly"})
    @PatchMapping("/weekly/{scheduleId}/toggle")
    public ResponseEntity<Void> toggleWeeklyScheduleAvailability(
            @PathVariable UUID carwashId,
            @PathVariable UUID scheduleId) {
        scheduleService.toggleWeeklyScheduleAvailability(carwashId, scheduleId);
        return ResponseEntity.ok().build();
    }

    // ===== SPECIAL DAYS =====
    @Operation(summary = "Get all special days", tags = {"Car Wash Profile - Schedule - Special Days"})
    @GetMapping("/special-days")
    public ResponseEntity<List<SpecialDayResponse>> getSpecialDays(
            @PathVariable UUID carwashId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "true") boolean activeOnly) {

        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(scheduleService.getSpecialDaysByDateRange(carwashId, startDate, endDate, activeOnly));
        }
        return ResponseEntity.ok(scheduleService.getAllSpecialDays(carwashId, activeOnly));
    }

    @Operation(summary = "Get a specific special day", tags = {"Car Wash Profile - Schedule - Special Days"})
    @GetMapping("/special-days/{specialDayId}")
    public ResponseEntity<SpecialDayResponse> getSpecialDay(
            @PathVariable UUID carwashId,
            @PathVariable UUID specialDayId,
            @RequestParam(defaultValue = "false") boolean includeInactive) {
        return ResponseEntity.ok(scheduleService.getSpecialDay(carwashId, specialDayId, includeInactive));
    }

    @Operation(summary = "Create special days", tags = {"Car Wash Profile - Schedule - Special Days"})
    @PostMapping("/special-days")
    public ResponseEntity<List<SpecialDayResponse>> createSpecialDays(
            @PathVariable UUID carwashId,
            @Valid @RequestBody List<SpecialDayRequest> requests) {
        return ResponseEntity.ok(scheduleService.createSpecialDays(carwashId, requests));
    }

    @Operation(summary = "Update special day", tags = {"Car Wash Profile - Schedule - Special Days"})
    @PutMapping("/special-days/{specialDayId}")
    public ResponseEntity<SpecialDayResponse> updateSpecialDay(
            @PathVariable UUID carwashId,
            @PathVariable UUID specialDayId,
            @Valid @RequestBody SpecialDayRequest request) {
        return ResponseEntity.ok(scheduleService.updateSpecialDay(carwashId, specialDayId, request));
    }

    @Operation(summary = "Disable special day", tags = {"Car Wash Profile - Schedule - Special Days"})
    @DeleteMapping("/special-days/{specialDayId}")
    public ResponseEntity<Void> deleteSpecialDay(
            @PathVariable UUID carwashId,
            @PathVariable UUID specialDayId) {
        scheduleService.deleteSpecialDay(carwashId, specialDayId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restore special day", tags = {"Car Wash Profile - Schedule - Special Days"})
    @PatchMapping("/special-days/{specialDayId}/restore")
    public ResponseEntity<Void> restoreSpecialDay(
            @PathVariable UUID carwashId,
            @PathVariable UUID specialDayId) {
        scheduleService.restoreSpecialDay(carwashId, specialDayId);
        return ResponseEntity.ok().build();
    }

    // ===== AVAILABILITY =====
    @Operation(summary = "Get available time slots", tags = {"Car Wash Profile - Schedule - Availability"})
    @GetMapping("/availability")
    public ResponseEntity<List<TimeSlotResponse>> getAvailableTimeSlots(
            @PathVariable UUID carwashId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.getAvailableTimeSlots(carwashId, date));
    }
}
