package com.icar.platform.api.controller.v1.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileUpdateRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.platform.application.service.carwash.profile.CarWashProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Car Wash Profile - General", description = "Manage basic profile info of the car wash")
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class GeneralProfileController {
    private final CarWashProfileService carWashProfileService;

    @PostMapping("/{profileId}")
    public ResponseEntity<CarWashProfileResponse> createProfile(
            @PathVariable UUID carwashId,
            @Valid @RequestBody CarWashProfileRequest request) {
        return ResponseEntity.ok(carWashProfileService.createProfile(carwashId, request));
    }

    @GetMapping("/subdomains/check")
    public ResponseEntity<Map<String, Boolean>> checkSubdomainAvailability(@RequestParam String subdomain) {
        boolean isAvailable = carWashProfileService.isSubdomainAvailable(subdomain);
        return ResponseEntity.ok(Map.of("available", isAvailable));
    }

    @GetMapping("/{carwashId}")
    public ResponseEntity<CarWashProfileResponse> getProfileByRegistrationId(@PathVariable UUID carwashId) {
        CarWashProfileResponse profileResponse = carWashProfileService.getProfileByCarWashRegistrationId(carwashId);
        return ResponseEntity.ok(profileResponse);
    }

    @GetMapping("/id/{profileId}")
    public ResponseEntity<CarWashProfileResponse> getProfileByProfileId(@PathVariable UUID profileId) {
        CarWashProfileResponse profile = carWashProfileService.getProfileById(profileId);
        return ResponseEntity.ok(profile);
    }


    @PutMapping(value = "/{profileId}", consumes = "multipart/form-data")
    public ResponseEntity<CarWashProfileResponse> updateProfile(
            @PathVariable UUID profileId,
            @RequestPart("data") @Valid CarWashProfileUpdateRequest request,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "coverPhoto", required = false) MultipartFile coverPhoto) {

        return ResponseEntity.ok(carWashProfileService.updateProfile(profileId, request, logo, coverPhoto));
    }


}