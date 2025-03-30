package com.icar.plataform.controller.v1;

import com.icar.plataform.dto.request.AppointmentRequest;
import com.icar.plataform.dto.response.AppointmentResponse;
import com.icar.plataform.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Appointments", description = "Appointment management operations")
@RestController
@RequestMapping("/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @Operation(summary = "Create new appointment")
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(
            @Valid @org.springframework.web.bind.annotation.RequestBody AppointmentRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @Operation(summary = "Get appointment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "List all appointments")
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
}