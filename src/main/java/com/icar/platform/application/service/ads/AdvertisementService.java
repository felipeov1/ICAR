package com.icar.platform.application.service.ads;

import com.icar.platform.api.dto.request.ads.AdvertisementRequest;
import com.icar.platform.api.dto.response.admin.PartnerSummaryResponse;
import com.icar.platform.api.dto.response.ads.AdvertisementResponse;
import com.icar.platform.domain.model.advertisement.Advertisement;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.repository.ads.AdvertisementRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.infrastructure.storage.StorageService;
import com.icar.platform.infrastructure.storage.config.StorageProperties;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final CarWashProfileRepository carWashProfileRepository;
    private final StorageService storageService;
    private final StorageProperties storageProperties;

    @Transactional(readOnly = true)
    public List<AdvertisementResponse> getAllAdvertisementsForAdmin() {
        return advertisementRepository.findAllByIsDeletedFalse().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdvertisementResponse createAdvertisement(AdvertisementRequest request, MultipartFile image) {
        Advertisement ad = new Advertisement();

        CarWashProfile profile = null;
        String imageOwnerId = "platform";

        if (request.getCarWashProfileId() != null) {
            profile = carWashProfileRepository.findById(request.getCarWashProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("CarWashProfile not found com ID: " + request.getCarWashProfileId()));
            imageOwnerId = profile.getId().toString();
            ad.setCarWashProfile(profile);
            ad.setPlatformAd(false);
        } else {
            ad.setCarWashProfile(null);
            ad.setPlatformAd(true);
        }

        String relativePath = "ads/" + imageOwnerId + "/adsphotos";
        String savedPath = storageService.store(image, relativePath);
        String dbPath = "uploads/" + savedPath;
        ad.setImageUrl(dbPath);

        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setLinkUrl(request.getLinkUrl());
        ad.setExpiresAt(request.getExpiresAt());
        ad.setActive(request.isActive());

        return toDto(advertisementRepository.save(ad));
    }

    @Transactional
    public AdvertisementResponse updateAdvertisement(UUID adId, AdvertisementRequest request, MultipartFile image) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found"));

        CarWashProfile profile = null;
        String imageOwnerId = "platform";
        if (ad.getCarWashProfile() != null) {
            imageOwnerId = ad.getCarWashProfile().getId().toString();
        }

        if (request.getCarWashProfileId() != null) {
            profile = carWashProfileRepository.findById(request.getCarWashProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("CarWashProfile not found"));
            imageOwnerId = profile.getId().toString();
            ad.setCarWashProfile(profile);
            ad.setPlatformAd(false);
        } else {
            ad.setCarWashProfile(null);
            ad.setPlatformAd(true);
        }


        if (image != null && !image.isEmpty()) {
            storageService.delete(ad.getImageUrl());
            String relativePath = "ads/" + imageOwnerId + "/adsphotos";
            String savedPath = storageService.store(image, relativePath);
            String dbPath = "uploads/" + savedPath;
            ad.setImageUrl(dbPath);
        }

        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setLinkUrl(request.getLinkUrl());
        ad.setExpiresAt(request.getExpiresAt());
        ad.setActive(request.isActive());

        return toDto(advertisementRepository.save(ad));
    }

    @Transactional
    public AdvertisementResponse toggleAdvertisementStatus(UUID adId, boolean isActive) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found"));
        ad.setActive(isActive);
        return toDto(advertisementRepository.save(ad));
    }

    @Transactional
    public void softDeleteAdvertisement(UUID adId) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found"));
        ad.setDeleted(true);
        ad.setActive(false);
        advertisementRepository.save(ad);
    }

    @Transactional(readOnly = true)
    public List<AdvertisementResponse> getActiveAdvertisements() {
        LocalDateTime now = LocalDateTime.now();
        return advertisementRepository.findActiveAdvertisements(now).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private AdvertisementResponse toDto(Advertisement ad) {
        PartnerSummaryResponse partnerSummary = null;
        if (ad.getCarWashProfile() != null) {
            partnerSummary = new PartnerSummaryResponse(
                    ad.getCarWashProfile().getId(),
                    ad.getCarWashProfile().getName()
            );
        }

        return AdvertisementResponse.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .description(ad.getDescription())
                .link(ad.getLinkUrl())
                .imageUrl(ad.getImageUrl())
                .partner(partnerSummary)
                .status(ad.isActive() ? "ACTIVE" : "INACTIVE")
                .expiresAt(ad.getExpiresAt())
                .startDate(ad.getCreatedAt())
                .build();
    }
}