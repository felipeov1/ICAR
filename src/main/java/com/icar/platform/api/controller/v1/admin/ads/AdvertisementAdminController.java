package com.icar.platform.api.controller.v1.admin.ads;

import com.icar.platform.api.dto.request.ads.AdvertisementRequest;
import com.icar.platform.api.dto.response.ads.AdvertisementResponse;
import com.icar.platform.application.service.ads.AdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Tag(name = "Admin - Advertisements", description = "Endpoints for managing advertisements")
@RestController
@RequestMapping("/api/v1/admin/advertisements")
@RequiredArgsConstructor
public class AdvertisementAdminController {

    private final AdvertisementService advertisementService;

    @GetMapping
    @Operation(summary = "Get all non-deleted advertisements for the admin panel")
    public ResponseEntity<List<AdvertisementResponse>> getAllAdvertisements() {
        return ResponseEntity.ok(advertisementService.getAllAdvertisementsForAdmin());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new advertisement")
    public ResponseEntity<AdvertisementResponse> createAdvertisement(
            @Valid @RequestPart("request") AdvertisementRequest request,
            @RequestPart("image") MultipartFile image) {
        AdvertisementResponse response = advertisementService.createAdvertisement(request, image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{adId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing advertisement")
    public ResponseEntity<AdvertisementResponse> updateAdvertisement(
            @PathVariable UUID adId,
            @Valid @RequestPart("request") AdvertisementRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        AdvertisementResponse response = advertisementService.updateAdvertisement(adId, request, image);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{adId}/status")
    @Operation(summary = "Activate or deactivate an advertisement")
    public ResponseEntity<AdvertisementResponse> updateAdvertisementStatus(
            @PathVariable UUID adId,
            @RequestParam boolean isActive) {
        AdvertisementResponse response = advertisementService.toggleAdvertisementStatus(adId, isActive);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{adId}")
    @Operation(summary = "Soft delete an advertisement (will not appear in the admin list)")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable UUID adId) {
        advertisementService.softDeleteAdvertisement(adId);
        return ResponseEntity.noContent().build();
    }
}