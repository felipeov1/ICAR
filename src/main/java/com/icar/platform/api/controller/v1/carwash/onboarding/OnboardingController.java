package com.icar.platform.api.controller.v1.carwash.onboarding;

import com.icar.platform.api.dto.response.auth.LoginResponse;
import com.icar.platform.application.service.carwash.onboarding.OnboardingService;
import com.icar.platform.api.dto.request.carwash.onboarding.OnboardingRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carwashes/{carWashId}/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LoginResponse> completeOnboarding(
            @PathVariable UUID carWashId,
            @RequestPart("data") @Valid OnboardingRequest request,
            @RequestPart("logo") MultipartFile logo,
            @RequestPart(value = "coverPhoto", required = false) MultipartFile coverPhoto,
            @RequestPart(value = "galleryFiles", required = false) List<MultipartFile> galleryFiles,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        LoginResponse response = onboardingService.processOnboarding(
                carWashId, request, logo, coverPhoto, galleryFiles, httpServletRequest, httpServletResponse
        );
        return ResponseEntity.ok(response);
    }
}
