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
            @Valid @RequestBody CarWashRegistrationRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @Operation(summary = "List all Car Washes ordered by creation date")
    @GetMapping
    public ResponseEntity<List<CarWashRegistrationResponse>> findAll() {
        return ResponseEntity.ok(service.findAllByOrderByCreatedAtAsc());
    }

    @Operation(summary = "Get car wash by subdomain")
    @GetMapping("/subdomain/{subdomain}")
    public ResponseEntity<CarWashRegistrationResponse> findBySubdomain(@PathVariable String subdomain) {
        return ResponseEntity.ok(service.findBySubdomain(subdomain));
    }

    @Operation(summary = "Get car wash details")
    @GetMapping("/{id}")
    public ResponseEntity<CarWashRegistrationResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Update car wash by id")
    @PutMapping("/{id}")
    public ResponseEntity<CarWashRegistrationResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody CarWashRegistrationRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Deactivate car wash by id (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}