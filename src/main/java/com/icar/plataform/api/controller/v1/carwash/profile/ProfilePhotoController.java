package com.icar.plataform.api.controller.v1.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.ProfilePhotoRequest;
import com.icar.plataform.application.service.carwash.profile.CarWashProfileService;
import com.icar.plataform.application.service.carwash.profile.PhotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Car Wash Profile - Photos", description = "Manage profile photos of the car wash")
@RestController
@RequestMapping("/api/v1/carwashes/{carwashId}/profile/photos")
@RequiredArgsConstructor
public class ProfilePhotoController {

    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<Void> addPhotos(
            @PathVariable UUID carwashId,
            @Valid @RequestBody ProfilePhotoRequest request) {
        photoService.addPhotos(carwashId, request.getPhotos());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removePhoto(
            @PathVariable UUID carwashId,
            @RequestParam String photoUrl) {
        photoService.removePhoto(carwashId, photoUrl);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> getPhotos(@PathVariable UUID carwashId) {
        return ResponseEntity.ok(photoService.getPhotos(carwashId));
    }
}