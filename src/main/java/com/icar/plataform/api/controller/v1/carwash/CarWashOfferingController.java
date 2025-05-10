package com.icar.plataform.api.controller.v1.carwash;

import com.icar.plataform.api.dto.request.carwash.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashOfferingResponse;
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
@RequestMapping("/api/v1/carwashes/{carWashProfileId}/offerings")
@RequiredArgsConstructor
public class CarWashOfferingController {
    private final CarWashOfferingService offeringService;

    @Operation(summary = "List all services for a car wash")
    @GetMapping
    public ResponseEntity<List<CarWashOfferingResponse>> findAllByCarWash(
            @PathVariable UUID carWashProfileId) {
        return ResponseEntity.ok(offeringService.findAllByCarWashProfile(carWashProfileId));
    }

    @Operation(summary = "Create a new service for a car wash")
    @PostMapping
    public ResponseEntity<CarWashOfferingResponse> create(
            @PathVariable UUID carWashProfileId,
            @Valid @RequestBody CarWashOfferingRequest request) {
        CarWashOfferingResponse response = offeringService.create(carWashProfileId, request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Update an offering")
    @PutMapping("/{offeringId}")
    public ResponseEntity<CarWashOfferingResponse> update(
            @PathVariable UUID carWashProfileId,
            @PathVariable UUID offeringId,
            @Valid @RequestBody CarWashOfferingRequest request) {
        return ResponseEntity.ok(offeringService.update(offeringId, request));
    }

    @Operation(summary = "Deactivate an offering")
    @DeleteMapping("/{offeringId}")
    public ResponseEntity<Void> deactivate(
            @PathVariable UUID carWashProfileId,
            @PathVariable UUID offeringId) {
        offeringService.deactivate(offeringId);
        return ResponseEntity.noContent().build();
    }

}
