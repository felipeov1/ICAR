package com.icar.platform.application.service.carwash.onboarding;

import com.icar.platform.api.dto.request.carwash.onboarding.OnboardingRequest;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

public interface OnboardingService {
    LoginResponse processOnboarding(
            UUID carWashId,
            OnboardingRequest onboardingRequest,
            MultipartFile logo,
            MultipartFile coverPhoto,
            List<MultipartFile> galleryFiles,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse);
}