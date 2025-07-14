package com.icar.platform.application.service.ads;

import com.icar.platform.api.dto.request.ads.AdvertisementRequest;
import com.icar.platform.api.dto.response.ads.AdvertisementResponse;
import com.icar.platform.domain.model.ads.Advertisement;
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

    @Transactional
    public AdvertisementResponse createAdvertisement(AdvertisementRequest request, MultipartFile image) {
        CarWashProfile profile = carWashProfileRepository.findById(request.getCarWashProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("CarWashProfile not found"));

        String relativePath = "ads/" + profile.getId().toString() + "/adsphotos";
        String imageUrl = storageService.store(image, relativePath);

        Advertisement ad = new Advertisement();
        ad.setCarWashProfile(profile);
        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setLinkUrl(request.getLinkUrl());
        ad.setExpiresAt(request.getExpiresAt());
        ad.setActive(request.isActive());
        ad.setPlatformAd(request.isPlatformAd());
        ad.setImageUrl(imageUrl);

        return toDto(advertisementRepository.save(ad));
    }

    @Transactional
    public AdvertisementResponse updateAdvertisement(UUID adId, AdvertisementRequest request, MultipartFile image) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found"));

        CarWashProfile profile = carWashProfileRepository.findById(request.getCarWashProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("CarWashProfile not found"));

        if (image != null && !image.isEmpty()) {
            String relativePath = "ads/" + profile.getId().toString() + "/adsphotos";
            String newImageUrl = storageService.store(image, relativePath);
            ad.setImageUrl(newImageUrl);
        }

        ad.setCarWashProfile(profile);
        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setLinkUrl(request.getLinkUrl());
        ad.setExpiresAt(request.getExpiresAt());
        ad.setActive(request.isActive());
        ad.setPlatformAd(request.isPlatformAd());

        return toDto(advertisementRepository.save(ad));
    }

    @Transactional
    public void softDeleteAdvertisement(UUID adId) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found"));

        ad.setActive(false);
        advertisementRepository.save(ad);
    }

    @Transactional(readOnly = true)
    public List<AdvertisementResponse> getActiveAdvertisements() {
        LocalDateTime now = LocalDateTime.now();
        List<Advertisement> activeAds = advertisementRepository.findActivePaidAds(now);

        if (activeAds.isEmpty()) {
            activeAds = advertisementRepository.findActivePlatformAds();
        }

        return activeAds.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private AdvertisementResponse toDto(Advertisement ad) {
        String fullImageUrl = "/files/" + ad.getImageUrl();

        return AdvertisementResponse.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .description(ad.getDescription())
                .imageUrl(fullImageUrl)
                .link(ad.getLinkUrl())
                .build();
    }
}