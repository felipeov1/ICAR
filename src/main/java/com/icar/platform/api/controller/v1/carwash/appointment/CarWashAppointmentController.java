package com.icar.platform.api.controller.v1.carwash.appointment;

import com.icar.platform.api.dto.request.appointment.AppointmentStatusUpdateRequest;
import com.icar.platform.api.dto.request.appointment.ManualAppointmentRequest;
import com.icar.platform.api.dto.request.carwash.profile.RescheduleByCompanyRequest;
import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.api.dto.response.carwash.CompanyAppointmentResponse;
import com.icar.platform.application.service.appointment.AppointmentService;
import com.icar.platform.application.service.appointment.AppointmentServiceImpl;
import com.icar.platform.application.service.payment.gateway.RefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final RefundService refundService;
    private final AppointmentServiceImpl carWashAppointmentService;

    @Operation(summary = "Get upcoming appointments (ordered from oldest to newest)")
    @GetMapping("/upcoming")
    public ResponseEntity<Page<CompanyAppointmentResponse>> getUpcomingAppointments(
            @PathVariable UUID carWashId, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsForCarWash(carWashId, pageable));
    }

    @Operation(summary = "Get completed appointments (ordered from newest to oldest)")
    @GetMapping("/completed")
    public ResponseEntity<Page<CompanyAppointmentResponse>> getCompletedAppointments(
            @PathVariable UUID carWashId, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getCompletedAppointmentsForCarWash(carWashId, pageable));
    }

    @Operation(summary = "Get canceled appointments (ordered from newest to oldest)")
    @GetMapping("/canceled")
    public ResponseEntity<Page<CompanyAppointmentResponse>> getCanceledAppointments(
            @PathVariable UUID carWashId, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getCanceledAppointmentsForCarWash(carWashId, pageable));
    }
    @Operation(summary = "Get refund pending appointments (ordered from newest to oldest)")
    @GetMapping("/refund-pending")
    public ResponseEntity<Page<CompanyAppointmentResponse>> getRefundPendingAppointments(
            @PathVariable UUID carWashId, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getRefundPendingAppointmentsForCarWash(carWashId, pageable));
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

    @Operation(summary = "Process a full refund for a canceled appointment")
    @PostMapping("/{appointmentId}/refund")
    public ResponseEntity<Void> processRefund(
            @PathVariable("carWashId") UUID profileId,
            @PathVariable UUID appointmentId) {
        refundService.processRefundForAppointment(appointmentId, profileId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cancel an appointment by the car wash")
    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointmentByCarWash(
            @PathVariable("carWashId") UUID profileId,
            @PathVariable UUID appointmentId) {

        AppointmentResponse response = carWashAppointmentService.cancelAppointmentByCompany(appointmentId, profileId);
        return ResponseEntity.ok(response);
    }
}
