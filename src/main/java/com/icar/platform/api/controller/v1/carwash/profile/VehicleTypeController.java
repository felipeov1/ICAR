package com.icar.platform.api.controller.v1.carwash.profile;

import com.icar.platform.application.service.carwash.profile.VehicleTypesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Car Wash Profile - Vehicle Types", description = "Manage vehicle types accepted by car wash")
@RestController
@RequestMapping("/api/v1/profile/{profileId}/vehicle-types")
@RequiredArgsConstructor
public class VehicleTypeController {

    private final VehicleTypesService vehicleTypesService;

    @GetMapping
    public ResponseEntity<List<String>> getVehicleTypes(@PathVariable UUID profileId) {
        List<String> vehicleTypes = vehicleTypesService.getVehicleTypesByProfileId(profileId);
        return ResponseEntity.ok(vehicleTypes);
    }

    @PutMapping
    public ResponseEntity<List<String>> updateVehicleTypes(
            @PathVariable UUID profileId,
            @RequestBody List<String> vehicleTypes) {
        List<String> updatedTypes = vehicleTypesService.updateVehicleTypesByProfileId(profileId, vehicleTypes);
        return ResponseEntity.ok(updatedTypes);
    }
}