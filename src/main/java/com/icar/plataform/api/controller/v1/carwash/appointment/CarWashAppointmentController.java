package com.icar.plataform.api.controller.v1.carwash.appointment;

import com.icar.plataform.api.dto.request.appointment.AppointmentStatusUpdateRequest;
import com.icar.plataform.api.dto.response.appointment.AppointmentResponse;
import com.icar.plataform.application.service.appointment.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Car Wash - Appointments", description = "Manage car wash appointments")
@RestController
@RequestMapping("/api/v1/carwashes/{carWashId}/appointments")
@RequiredArgsConstructor
public class CarWashAppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Get upcoming appointments (ordered from oldest to newest)")
    @GetMapping("/upcoming")
    public ResponseEntity<List<AppointmentResponse>> getUpcomingAppointments(
            @PathVariable UUID carWashId) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsForCarWash(carWashId));
    }

    @Operation(summary = "Get completed appointments (ordered from newest to oldest)")
    @GetMapping("/completed")
    public ResponseEntity<List<AppointmentResponse>> getCompletedAppointments(
            @PathVariable UUID carWashId) {
        return ResponseEntity.ok(appointmentService.getCompletedAppointmentsForCarWash(carWashId));
    }

    @Operation(summary = "Get canceled appointments (ordered from newest to oldest)")
    @GetMapping("/canceled")
    public ResponseEntity<List<AppointmentResponse>> getCanceledAppointments(
            @PathVariable UUID carWashId) {
        return ResponseEntity.ok(appointmentService.getCanceledAppointmentsForCarWash(carWashId));
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

}
