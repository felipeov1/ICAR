package com.icar.platform.api.controller.v1.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.PhotoDTO;
import com.icar.platform.api.dto.response.carwash.profile.PhotoServicesResponse;
import com.icar.platform.application.service.carwash.profile.PhotoService;
import com.icar.platform.domain.enums.PhotoType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Car Wash Profile - Photos", description = "Manage profile photos of the car wash")
@RestController
@RequestMapping("/api/v1/carwashes/{carwashId}/profile/photos")
@RequiredArgsConstructor
public class ProfilePhotoController {

    private final PhotoService photoService;


    @PostMapping(value = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoDTO> uploadLogo(
            @PathVariable UUID carwashId,
            @RequestParam("file") MultipartFile file) {
        photoService.addPhoto(carwashId, file, PhotoType.LOGO);
        return ResponseEntity.ok(new PhotoDTO("Logo updated successfully"));
    }

    @PostMapping(value = "/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoDTO> uploadCover(
            @PathVariable UUID carwashId,
            @RequestParam("file") MultipartFile file) {
        photoService.addPhoto(carwashId, file, PhotoType.COVER);
        return ResponseEntity.ok(new PhotoDTO("Cover photo updated successfully"));
    }


    @GetMapping("/logo")
    public ResponseEntity<PhotoDTO> getLogo(@PathVariable UUID carwashId) {
        List<PhotoDTO> logos = photoService.getPhotosByType(carwashId, PhotoType.LOGO);
        return ResponseEntity.ok(logos.isEmpty() ? null : logos.getFirst());
    }

    @GetMapping("/cover")
    public ResponseEntity<PhotoDTO> getCover(@PathVariable UUID carwashId) {
        List<PhotoDTO> covers = photoService.getPhotosByType(carwashId, PhotoType.COVER);
        return ResponseEntity.ok(covers.isEmpty() ? null : covers.getFirst());
    }

    @GetMapping("/services")
    @Transactional
    public ResponseEntity<List<PhotoServicesResponse>> getServicePhotos(@PathVariable UUID carwashId) {
        List<PhotoServicesResponse> photos = photoService.getServicePhotos(carwashId);
        return ResponseEntity.ok(photos);
    }

    @PostMapping(value = "/services", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PhotoServicesResponse>> uploadServicePhotos(
            @PathVariable UUID carwashId,
            @RequestParam("files") List<MultipartFile> files) {
        List<PhotoServicesResponse> responses = photoService.addServicePhotos(carwashId, files);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{photoUrl}")
    public ResponseEntity<Void> removePhoto(
            @PathVariable UUID carwashId,
            @PathVariable String photoUrl) {
        photoService.removePhoto(carwashId, photoUrl);
        return ResponseEntity.noContent().build();
    }
}