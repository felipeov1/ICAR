package com.icar.platform.api.controller.v1.customer;

import com.icar.platform.api.dto.request.appointment.AppointmentRequest;
import com.icar.platform.api.dto.request.appointment.RescheduleRequest;
import com.icar.platform.api.dto.response.appointment.AppointmentResponse;
import com.icar.platform.application.service.appointment.AppointmentService;
import com.icar.platform.infrastructure.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customer/appointments")
@RequiredArgsConstructor
public class CustomerAppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Create a new appointment")
    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestBody @Valid AppointmentRequest request
    ) {
        UUID customerId = user.getId();
        AppointmentResponse createdAppointment = appointmentService.createAppointment(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
    }

    @Operation(summary = "Get customer's upcoming appointments")
    @GetMapping("/upcoming")
    public ResponseEntity<List<AppointmentResponse>> getUpcomingAppointments(
            @AuthenticationPrincipal UserDetailsImpl user
    ) {
        UUID customerId = user.getId();
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsForCustomer(customerId));
    }

    @Operation(summary = "Get customer's appointment history")
    @GetMapping("/history")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentHistory(
            @AuthenticationPrincipal UserDetailsImpl user
    ) {
        UUID customerId = user.getId();
        return ResponseEntity.ok(appointmentService.getCompletedOrCanceledAppointmentsForCustomer(customerId));
    }

    @Operation(summary = "Cancel an appointment")
    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable UUID appointmentId
    ) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId, user.getId()));
    }

    @Operation(summary = "Reschedule an appointment")
    @PatchMapping("/{appointmentId}/reschedule")
    public ResponseEntity<AppointmentResponse> rescheduleAppointment(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable UUID appointmentId,
            @RequestBody @Valid RescheduleRequest request
    ) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(appointmentId, user.getId(), request.newDateTime()));
    }
}