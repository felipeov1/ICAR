package com.icar.plataform.api.controller.v1.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.PhotoDTO;
import com.icar.plataform.application.service.carwash.profile.PhotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<Void> addPhotos(
            @PathVariable UUID carwashId,
            @RequestParam("files") List<MultipartFile> files) {
        photoService.addPhotos(carwashId, files);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removePhoto(
            @PathVariable UUID carwashId,
            @RequestParam String photoName) {  // ALTERAÇÃO AQUI
        photoService.removePhoto(carwashId, photoName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PhotoDTO>> getPhotos(@PathVariable UUID carwashId) {
        List<PhotoDTO> photos = photoService.getPhotos(carwashId);
        return ResponseEntity.ok(photos);
    }
}
