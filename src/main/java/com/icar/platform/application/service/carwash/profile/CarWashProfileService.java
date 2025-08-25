package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileUpdateRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashProfileResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CarWashProfileService {
    CarWashProfileResponse createProfile(@NonNull UUID carWashId, @NonNull CarWashProfileRequest request);
    CarWashProfileResponse getProfileByCarWashRegistrationId(UUID carWashId);
    boolean isSubdomainAvailable(String subdomain);
    CarWashProfileResponse getProfileById(@NonNull UUID profileId);
    CarWashProfileResponse updateProfile(@NonNull UUID profileId, @NonNull CarWashProfileUpdateRequest request, MultipartFile logo, MultipartFile coverPhoto);
    List<CarWashProfileResponse> findAllForMarketplace();
    CarWashProfileResponse getProfileBySubdomain(String subdomain);

}
