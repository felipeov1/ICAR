package com.icar.plataform.api.controller.v1.customer.appointment;

import com.icar.plataform.api.dto.request.appointment.AppointmentRequest;
import com.icar.plataform.api.dto.request.appointment.AppointmentRescheduleRequest;
import com.icar.plataform.api.dto.response.appointment.AppointmentResponse;
import com.icar.plataform.application.service.appointment.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Customer - Appointments", description = "Manage customer appointments")
@RestController
@RequestMapping("/api/v1/customers/{customerId}/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Create new appointment")
    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @PathVariable UUID customerId,
            @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.createAppointment(customerId, request));
    }

    @Operation(summary = "Get upcoming appointments (ordered from oldest to newest)")
    @GetMapping("/upcoming")
    public ResponseEntity<List<AppointmentResponse>> getUpcomingAppointments(
            @PathVariable UUID customerId) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsForCustomer(customerId));
    }

    @Operation(summary = "Get completed and canceled appointments (ordered from newest to oldest)")
    @GetMapping("/completed")
    public ResponseEntity<List<AppointmentResponse>> getCompletedAndCanceledAppointments(
            @PathVariable UUID customerId) {
        return ResponseEntity.ok(appointmentService.getCompletedOrCanceledAppointmentsForCustomer(customerId));
    }

    @Operation(summary = "Cancel appointment")
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable UUID customerId,
            @PathVariable UUID appointmentId) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId, customerId));
    }

    @Operation(summary = "Reschedule appointment")
    @PutMapping("/{appointmentId}/reschedule")
    public ResponseEntity<AppointmentResponse> rescheduleAppointment(
            @PathVariable UUID customerId,
            @PathVariable UUID appointmentId,
            @RequestBody @Valid AppointmentRescheduleRequest request) {
        return ResponseEntity.ok(
                appointmentService.rescheduleAppointment(appointmentId, customerId, request.newDateTime())
        );
    }
}