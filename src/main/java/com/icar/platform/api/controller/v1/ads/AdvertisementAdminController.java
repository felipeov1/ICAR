package com.icar.platform.api.controller.v1.ads;

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

import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Admin - Advertisements", description = "Endpoints for managing advertisements")
@RestController
@RequestMapping("/api/v1/admin/advertisements")
@RequiredArgsConstructor
public class AdvertisementAdminController {

    private final AdvertisementService advertisementService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new advertisement")
    public ResponseEntity<AdvertisementResponse> createAdvertisement(

            @RequestParam("carWashProfileId") UUID carWashProfileId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("linkUrl") String linkUrl,
            @RequestParam("expiresAt") LocalDateTime expiresAt,
            @RequestParam(value = "isActive", defaultValue = "true") boolean isActive,
            @RequestParam(value = "isPlatformAd", defaultValue = "false") boolean isPlatformAd,
            @RequestPart("image") MultipartFile image) {

        AdvertisementRequest request = new AdvertisementRequest();
        request.setCarWashProfileId(carWashProfileId);
        request.setTitle(title);
        request.setDescription(description);
        request.setLinkUrl(linkUrl);
        request.setExpiresAt(expiresAt);
        request.setActive(isActive);
        request.setPlatformAd(isPlatformAd);

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

    @DeleteMapping("/{adId}")
    @Operation(summary = "Deactivate (soft delete) an advertisement")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable UUID adId) {
        advertisementService.softDeleteAdvertisement(adId);
        return ResponseEntity.noContent().build();
    }
}