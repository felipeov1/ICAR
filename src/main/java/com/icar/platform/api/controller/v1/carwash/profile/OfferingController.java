package com.icar.platform.api.controller.v1.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashOfferingResponse;
import com.icar.platform.application.service.carwash.offering.CarWashOfferingService;
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

@Tag(name = "Car Wash Profile - Offerings", description = "Manage car wash services and offerings")
@RestController
@RequestMapping("/api/v1/profile/{profileId}/offerings")
@RequiredArgsConstructor
public class OfferingController {

    private final CarWashOfferingService offeringService;

    @Operation(summary = "Get all offerings for a car wash")
    @GetMapping
    public ResponseEntity<List<CarWashOfferingResponse>> findAllOfferings(@PathVariable UUID profileId) {
        return ResponseEntity.ok(offeringService.findAllByProfileId(profileId));
    }

    @Operation(summary = "Get offerings for a specific vehicle type")
    @GetMapping("/vehicle-type/{vehicleType}")
    public ResponseEntity<List<CarWashOfferingResponse>> findOfferingsByVehicleType(
            @PathVariable UUID profileId,
            @PathVariable String vehicleType) {
        return ResponseEntity.ok(offeringService.findByProfileIdAndVehicleType(profileId, vehicleType));
    }

    @Operation(summary = "Create a new offering")
    @PostMapping
    public ResponseEntity<CarWashOfferingResponse> createOffering(
            @PathVariable UUID profileId,
            @Valid @RequestBody CarWashOfferingRequest request) {
        CarWashOfferingResponse response = offeringService.create(profileId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Update an existing offering")
    @PutMapping("/{offeringId}")
    public ResponseEntity<CarWashOfferingResponse> updateOffering(
            @PathVariable UUID profileId,
            @PathVariable UUID offeringId,
            @Valid @RequestBody CarWashOfferingRequest request) {
        return ResponseEntity.ok(offeringService.update(offeringId, request));
    }

    @Operation(summary = "Deactivate an offering")
    @PostMapping("/{offeringId}/deactivate")
    public ResponseEntity<Void> deactivateOffering(
            @PathVariable UUID profileId,
            @PathVariable UUID offeringId) {
        offeringService.deactivate(offeringId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activate an offering")
    @PatchMapping("/{offeringId}/activate")
    public ResponseEntity<Void> activateOffering(
            @PathVariable UUID profileId,
            @PathVariable UUID offeringId) {
        offeringService.activate(offeringId);
        return ResponseEntity.noContent().build();
    }
}