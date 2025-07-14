package com.icar.platform.api.controller.v1.marketplace;

import com.icar.platform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.platform.application.service.carwash.profile.CarWashProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Marketplace", description = "Endpoints for customers to browse car washes and services")
@RestController
@RequestMapping("/api/v1/marketplace")
@RequiredArgsConstructor
public class MarketplaceController {

    private final CarWashProfileService carWashProfileService;

    @Operation(summary = "List all available car washes", description = "Returns a list of all active and verified car wash profiles for the marketplace.")
    @GetMapping("/carwashes")
    public ResponseEntity<List<CarWashProfileResponse>> getAllCarWashes() {
        List<CarWashProfileResponse> carWashes = carWashProfileService.findAllForMarketplace();
        return ResponseEntity.ok(carWashes);
    }
}