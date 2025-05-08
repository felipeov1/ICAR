package com.icar.plataform.api.controller.v1;

import com.icar.plataform.api.dto.request.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.CarWashOfferingResponse;
import com.icar.plataform.application.service.carwash.CarWashOfferingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Car Wash Offerings", description = "Car wash offerings management")
@RestController
@RequestMapping("/v1/carwashes/{carWashId}/offerings")
@RequiredArgsConstructor
public class CarWashOfferingController {
    private final CarWashOfferingService service;

    @Operation(summary = "Create a new service for a car wash")
    @PostMapping
    public ResponseEntity<CarWashOfferingResponse> create(
            @PathVariable UUID carWashId,
            @Valid @RequestBody CarWashOfferingRequest request) {
        CarWashOfferingResponse response = service.create(carWashId, request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Update a offering")
    @PutMapping("/{offeringsId}")
    public ResponseEntity<CarWashOfferingResponse> update(
            @PathVariable UUID carWashId,
            @PathVariable UUID serviceId,
            @Valid @RequestBody CarWashOfferingRequest request) {
        return ResponseEntity.ok(service.update(serviceId, request));
    }

    @Operation(summary = "Deactivate a offerings")
    @DeleteMapping("/{offeringsId}")
    public ResponseEntity<Void> deactivate(
            @PathVariable UUID carWashId,
            @PathVariable UUID serviceId) {
        service.deactivate(serviceId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all services for a car wash")
    @GetMapping
    public ResponseEntity<List<CarWashOfferingResponse>> findAllByCarWash(
            @PathVariable UUID carWashId) {
        return ResponseEntity.ok(service.findAllByCarWash(carWashId));
    }

}
