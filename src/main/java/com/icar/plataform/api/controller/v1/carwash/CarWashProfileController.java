package com.icar.plataform.api.controller.v1.carwash;

import com.icar.plataform.application.service.carwashprofile.CarWashProfileService;
import com.icar.plataform.api.dto.request.carwash.CarWashProfileRequest;
import com.icar.plataform.api.dto.response.carwash.CarWashProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carwashes")
@RequiredArgsConstructor
public class CarWashProfileController {

    private final CarWashProfileService carWashProfileService;

    @PostMapping("/{carWashId}/profile")
    public CarWashProfileResponse createProfile(@PathVariable UUID carWashId,
                                                @RequestBody CarWashProfileRequest request) {
        return carWashProfileService.createProfile(carWashId, request);
    }

    @GetMapping("/{carWashId}/profile")
    public CarWashProfileResponse getProfile(@PathVariable UUID carWashId) {
        return carWashProfileService.getProfileByCarWashId(carWashId);
    }
}
