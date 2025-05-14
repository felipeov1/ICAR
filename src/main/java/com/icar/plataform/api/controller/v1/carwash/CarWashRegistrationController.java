package com.icar.plataform.api.controller.v1.carwash;

import com.icar.plataform.api.dto.request.carwash.CarWashRegistrationRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashRegistrationResponse;
import com.icar.plataform.application.service.carwash.legal.CarWashRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Car Wash - Legal Information", description = "Car wash establishment operations")
@RestController
@RequestMapping("/api/v1/carwashes")
@RequiredArgsConstructor
public class CarWashRegistrationController {

    private final CarWashRegistrationService service;

    @Operation(summary = "Register new car wash")
    @PostMapping
    public ResponseEntity<CarWashRegistrationResponse> create(
            @Valid @org.springframework.web.bind.annotation.RequestBody CarWashRegistrationRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @Operation(summary = "List all Car Washes ordered by creation date")
    @GetMapping("/all")
    public ResponseEntity<List<CarWashRegistrationResponse>> findAll() {
        return ResponseEntity.ok(service.findAllByOrderByCreatedAtAsc());
    }

    @Operation(summary = "Get car wash details")
    @GetMapping("/{id}")
    public ResponseEntity<CarWashRegistrationResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }
}