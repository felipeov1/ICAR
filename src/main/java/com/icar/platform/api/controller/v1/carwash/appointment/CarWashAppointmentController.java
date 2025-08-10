package com.icar.platform.api.controller.v1.carwash.appointment;

import com.icar.platform.api.dto.request.appointment.AppointmentStatusUpdateRequest;
import com.icar.platform.api.dto.request.appointment.ManualAppointmentRequest;
import com.icar.platform.api.dto.request.carwash.profile.RescheduleByCompanyRequest;
import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.api.dto.response.carwash.CompanyAppointmentResponse;
import com.icar.platform.application.service.appointment.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Car Wash - Appointments", description = "Manage car wash appointments")
@RestController
@RequestMapping("/api/v1/carwashes/{carWashId}/appointments")
@RequiredArgsConstructor
public class CarWashAppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Get upcoming appointments (ordered from oldest to newest)")
    @GetMapping("/upcoming")
    public ResponseEntity<List<CompanyAppointmentResponse>> getUpcomingAppointments(
            @PathVariable UUID carWashId) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsForCarWash(carWashId));
    }

    @Operation(summary = "Get completed appointments (ordered from newest to oldest)")
    @GetMapping("/completed")
    public ResponseEntity<List<CompanyAppointmentResponse>> getCompletedAppointments(
            @PathVariable UUID carWashId) {
        return ResponseEntity.ok(appointmentService.getCompletedAppointmentsForCarWash(carWashId));
    }

    @Operation(summary = "Get canceled appointments (ordered from newest to oldest)")
    @GetMapping("/canceled")
    public ResponseEntity<List<CompanyAppointmentResponse>> getCanceledAppointments(
            @PathVariable UUID carWashId) {
        return ResponseEntity.ok(appointmentService.getCanceledAppointmentsForCarWash(carWashId));
    }
    @Operation(summary = "Get refund pending appointments (ordered from newest to oldest)")
    @GetMapping("/refund-pending")
    public ResponseEntity<List<CompanyAppointmentResponse>> getRefundPendingAppointments(
            @PathVariable UUID carWashId) {
        return ResponseEntity.ok(appointmentService.getRefundPendingAppointmentsForCarWash(carWashId));
    }

    @Operation(summary = "Get count of refund pending appointments")
    @GetMapping("/refund-pending/count")
    public ResponseEntity<Map<String, Long>> getRefundPendingCount(
            @PathVariable UUID carWashId) {
        return ResponseEntity.ok(appointmentService.getRefundPendingCountForCarWash(carWashId));
    }

    @Operation(summary = "Reschedule an appointment by the car wash")
    @PatchMapping("/{appointmentId}/reschedule")
    public ResponseEntity<AppointmentResponse> rescheduleAppointmentByCompany(
            @PathVariable("carWashId") UUID profileId,
            @PathVariable UUID appointmentId,
            @Valid @RequestBody RescheduleByCompanyRequest request) {

        AppointmentResponse response = appointmentService.rescheduleAppointmentByCompany(profileId, appointmentId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Complete appointment")
    @PatchMapping("/{appointmentId}/complete")
    public ResponseEntity<AppointmentResponse> completeAppointment(
            @PathVariable UUID carWashId,
            @PathVariable UUID appointmentId,
            @RequestBody(required = false) AppointmentStatusUpdateRequest request) {

        if (request == null) {
            request = new AppointmentStatusUpdateRequest(appointmentId, carWashId, null, null);
        }
        return ResponseEntity.ok(appointmentService.completeAppointment(appointmentId));
    }

    @GetMapping("/by-month")
    public ResponseEntity<List<CompanyAppointmentResponse>> getAppointmentsByMonth(
            @PathVariable UUID carWashId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByMonth(carWashId, year, month));
    }

    @Operation(summary = "Create a manual appointment by the car wash staff")
    @PostMapping("/manual")
    public ResponseEntity<AppointmentResponse> createManualAppointment(
            @PathVariable UUID carWashId,
            @Valid @RequestBody ManualAppointmentRequest request) {
        return ResponseEntity.status(201).body(appointmentService.createManualAppointment(carWashId, request));
    }

    @Operation(summary = "Confirm that a refund has been processed")
    @PatchMapping("/{appointmentId}/confirm-refund")
    public ResponseEntity<AppointmentResponse> confirmRefund(
            @PathVariable UUID carWashId,
            @PathVariable UUID appointmentId) {
        return ResponseEntity.ok(appointmentService.confirmRefund(appointmentId, carWashId));
    }


}
