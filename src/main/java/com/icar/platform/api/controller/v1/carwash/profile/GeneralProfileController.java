package com.icar.platform.api.controller.v1.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.platform.application.service.carwash.profile.CarWashProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Car Wash Profile - General", description = "Manage basic profile info of the car wash")
@RestController
@RequestMapping("/api/v1/carwashes/{carwashId}/profile")
@RequiredArgsConstructor
public class GeneralProfileController {
    private final CarWashProfileService carWashProfileService;

    @PostMapping
    public ResponseEntity<CarWashProfileResponse> createProfile(
            @PathVariable UUID carwashId,
            @Valid @RequestBody CarWashProfileRequest request) {
        return ResponseEntity.ok(carWashProfileService.createProfile(carwashId, request));
    }

    @GetMapping
    public ResponseEntity<CarWashProfileResponse> getProfile(@PathVariable UUID carwashId) {
        return ResponseEntity.ok(carWashProfileService.getProfileById(carwashId));
    }

    @PutMapping
    public ResponseEntity<CarWashProfileResponse> updateProfile(
            @PathVariable UUID carwashId,
            @Valid @RequestBody CarWashProfileRequest request) {
        return ResponseEntity.ok(carWashProfileService.updateProfile(carwashId, request));
    }
}