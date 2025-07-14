package com.icar.platform.api.controller.v1.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.AppointmentConfigRequest;
import com.icar.platform.api.dto.response.carwash.profile.AppointmentConfigResponse;
import com.icar.platform.application.service.carwash.profile.AppointmentConfigService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carwashes/{carwashId}/appointment-config")
@RequiredArgsConstructor
public class AppointmentConfigController {

    private final AppointmentConfigService appointmentConfigService;

    @Operation(summary = "Create appointment configuration", tags = {"Car Wash Profile - Appointment Config"})
    @PostMapping
    public ResponseEntity<AppointmentConfigResponse> createConfig(
            @PathVariable UUID carwashId,
            @Valid @RequestBody AppointmentConfigRequest request) {

        AppointmentConfigResponse response = appointmentConfigService.createConfig(carwashId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get appointment configuration", tags = {"Car Wash Profile - Appointment Config"})
    @GetMapping
    public ResponseEntity<AppointmentConfigResponse> getConfig(
            @PathVariable UUID carwashId) {
        return ResponseEntity.ok(appointmentConfigService.getConfig(carwashId));
    }

    @Operation(summary = "Update appointment configuration", tags = {"Car Wash Profile - Appointment Config"})
    @PutMapping
    public ResponseEntity<AppointmentConfigResponse> updateConfig(
            @PathVariable UUID carwashId,
            @Valid @RequestBody AppointmentConfigRequest request) {
        return ResponseEntity.ok(appointmentConfigService.updateConfig(carwashId, request));
    }
}