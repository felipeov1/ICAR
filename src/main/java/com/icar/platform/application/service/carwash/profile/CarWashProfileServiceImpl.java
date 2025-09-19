package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileRequest;
import com.icar.platform.api.dto.request.carwash.profile.CarWashProfileUpdateRequest;
import com.icar.platform.api.dto.response.carwash.profile.CarWashProfileResponse;
import com.icar.platform.api.mapper.carwash.CarWashProfileMapper;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.infrastructure.storage.FileSystemStorageService;
import com.icar.platform.shared.exception.DuplicateEntityException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarWashProfileServiceImpl implements CarWashProfileService {

    private final CarWashProfileRepository profileRepository;
    private final CarWashRegistrationDataRepository registrationRepository;
    private final CarWashProfileMapper mapper;
    private final FileSystemStorageService fileStorageService;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public CarWashProfileResponse createProfile(@NonNull UUID carWashId, @NonNull CarWashProfileRequest request) {
        CarWashRegistration registration = registrationRepository.findById(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash registration not found"));

        if (profileRepository.findProfileIdByRegistrationId(carWashId).isPresent()) {
            throw new DuplicateEntityException("Profile already exists for this car wash", "car_wash_profile", "car_wash_id");
        }

        CarWashProfile profile = mapper.toEntity(request);
        profile.setCarWashRegistration(registration);
        return mapper.toDto(profileRepository.save(profile));
    }

    @Override
    @Transactional(readOnly = true)
    public CarWashProfileResponse getProfileByCarWashRegistrationId(UUID carWashId) {
        CarWashProfile profile = profileRepository.findByCarWashRegistrationId(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("..."));
        entityManager.refresh(profile.getCarWashRegistration());
        return mapper.toDto(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSubdomainAvailable(String subdomain) {
        return !profileRepository.existsBySubdomain(subdomain);
    }

    @Override
    @Transactional(readOnly = true)
    public CarWashProfileResponse getProfileById(@NonNull UUID profileId) {
        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return mapper.toDto(profile);
    }

    @Override
    @Transactional
    public CarWashProfileResponse updateProfile(
            @NonNull UUID profileId,
            @NonNull CarWashProfileUpdateRequest request,
            MultipartFile logo,
            MultipartFile coverPhoto) {

        CarWashProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));

        mapper.updateEntity(request, profile);

        if (request.getWhatsapp() != null) {
            profile.setWhatsapp(request.getWhatsapp().replaceAll("[^0-9]", ""));
        }

        UUID carWashId = profile.getCarWashRegistration().getId();
        String commonPath = "carwash/" + carWashId.toString() + "/logoandcover";

        if (logo != null && !logo.isEmpty()) {
            String logoRelativePath = fileStorageService.store(logo, commonPath);
            profile.setLogo(logoRelativePath);
        }

        if (coverPhoto != null && !coverPhoto.isEmpty()) {
            String coverPhotoRelativePath = fileStorageService.store(coverPhoto, commonPath);
            profile.setCoverPhoto(coverPhotoRelativePath);
        }

        if (request.getLocations() != null) {
            profile.setLocations(request.getLocations().toArray(new String[0]));
        }
        if (request.getModalities() != null) {
            profile.setModalities(request.getModalities().toArray(new String[0]));
        }

        if (request.getWetWashObservations() != null) {
            profile.setWetWashObservations(request.getWetWashObservations());
        }
        if (request.getDryWashObservations() != null) {
            profile.setDryWashObservations(request.getDryWashObservations());
        }

        CarWashProfile updatedProfile = profileRepository.save(profile);
        return mapper.toDto(updatedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarWashProfileResponse> findAllForMarketplace() {

        Comparator<CarWashProfileResponse> marketplaceSort = Comparator
                .comparingInt((CarWashProfileResponse company) -> {
                    boolean isNewcomer = company.reviews() == 0 && company.createdAt().isAfter(LocalDateTime.now().minusDays(15));
                    return isNewcomer ? 0 : 1;
                })
                .thenComparing(CarWashProfileResponse::rating, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(CarWashProfileResponse::reviews, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(CarWashProfileResponse::createdAt);

        return profileRepository.findAll().stream()
                .filter(profile -> !profile.getSubdomain().equals("test") && !profile.getSubdomain().equals("rickydetail"))
                .map(mapper::toDto)
                .sorted(marketplaceSort)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CarWashProfileResponse getProfileBySubdomain(String subdomain) {
        CarWashProfile profile = profileRepository.findBySubdomain(subdomain)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for subdomain: " + subdomain));
        return mapper.toDto(profile);
    }
}