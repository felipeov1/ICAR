package com.icar.platform.api.controller.v1.ads;

import com.icar.platform.api.dto.response.ads.AdvertisementResponse;
import com.icar.platform.application.service.ads.AdvertisementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Advertisements", description = "Endpoints for displaying advertisements")
@RestController
@RequestMapping("/api/v1/marketplace/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping
    public ResponseEntity<List<AdvertisementResponse>> getActiveAdvertisements() {
        return ResponseEntity.ok(advertisementService.getActiveAdvertisements());
    }
}