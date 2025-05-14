package com.icar.plataform.api.controller.v1.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashOfferingRequest;
import com.icar.plataform.api.dto.response.carwash.profile.CarWashOfferingResponse;
import com.icar.plataform.application.service.carwash.offering.CarWashOfferingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Car Wash Profile - Offerings", description = "Manage available offerings/packages")
@RestController
@RequestMapping("/api/v1/carwashes/{carwashId}/profile/offerings")
@RequiredArgsConstructor
public class OfferingController {

    private final CarWashOfferingService offeringService;

    // List all offerings for this car wash
    @GetMapping
    public ResponseEntity<List<CarWashOfferingResponse>> findAllOfferings(@PathVariable UUID carwashId) {
        return ResponseEntity.ok(offeringService.findAllByCarWashProfile(carwashId));
    }

    // Create a new offering
    @PostMapping
    public ResponseEntity<CarWashOfferingResponse> createOffering(
            @PathVariable UUID carwashId,
            @Valid @RequestBody CarWashOfferingRequest request) {
        CarWashOfferingResponse response = offeringService.create(carwashId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    // Update an existing offering
    @PutMapping("/{offeringId}")
    public ResponseEntity<CarWashOfferingResponse> updateOffering(
            @PathVariable UUID carwashId,
            @PathVariable UUID offeringId,
            @Valid @RequestBody CarWashOfferingRequest request) {
        return ResponseEntity.ok(offeringService.update(offeringId, request));
    }

    // Delete (deactivate) an offering
    @DeleteMapping("/{offeringId}")
    public ResponseEntity<Void> deleteOffering(
            @PathVariable UUID carwashId,
            @PathVariable UUID offeringId) {
        offeringService.deactivate(offeringId);
        return ResponseEntity.noContent().build();
    }
}
