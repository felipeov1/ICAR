package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.PhotoDTO;
import com.icar.platform.api.dto.response.carwash.profile.OptimizedPhotoResponse;
import com.icar.platform.api.dto.response.carwash.profile.PhotoServicesResponse;
import com.icar.platform.domain.enums.PhotoType;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.infrastructure.storage.FileSystemStorageService;
import com.icar.platform.infrastructure.storage.config.StorageProperties;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final CarWashProfileRepository profileRepository;
    private final FileSystemStorageService storageService;
    private final StorageProperties storageProperties;

    @Override
    @Transactional
    public List<PhotoServicesResponse> addServicePhotos(UUID carWashId, List<MultipartFile> files) {
        CarWashProfile profile = getProfileByCarWashId(carWashId);

        files.forEach(file -> {
            Map<String, String> paths = storageService.storeAndCreateThumbnail(file, getPhotoPath(carWashId, PhotoType.SERVICE));

            String originalPath = paths.get("original");
            profile.getPhotos().add(originalPath);
        });

        profileRepository.save(profile);

        return profile.getPhotos().stream()
                .map(url -> new PhotoServicesResponse(buildFullUrl(url), "Uploaded", LocalDateTime.now()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoServicesResponse> getServicePhotos(UUID carWashId) {
        CarWashProfile profile = getProfileByCarWashId(carWashId);
        Hibernate.initialize(profile.getPhotos());

        return profile.getPhotos().stream()
                .map(url -> new PhotoServicesResponse(
                        buildFullUrl(url),
                        "Service photo",
                        profile.getUpdatedAt() != null ? profile.getUpdatedAt() : LocalDateTime.now()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addPhoto(UUID carWashId, MultipartFile file, PhotoType type) {
        CarWashProfile profile = getProfileByCarWashId(carWashId);

        switch (type) {
            case LOGO:
                String logoUrl = storageService.store(file, getPhotoPath(carWashId, type));
                if (profile.getLogo() != null) {
                    storageService.delete(profile.getLogo());
                }
                profile.setLogo(logoUrl);
                break;
            case COVER:
                String coverUrl = storageService.store(file, getPhotoPath(carWashId, type));
                if (profile.getCoverPhoto() != null) {
                    storageService.delete(profile.getCoverPhoto());
                }
                profile.setCoverPhoto(coverUrl);
                break;
            case SERVICE:
                Map<String, String> paths = storageService.storeAndCreateThumbnail(file, getPhotoPath(carWashId, type));
                profile.getPhotos().add(paths.get("original"));
                break;
        }

        profileRepository.save(profile);
    }

    private String getPhotoPath(UUID carWashId, PhotoType type) {
        String basePath = "carwash/" + carWashId;
        return switch (type) {
            case LOGO, COVER -> basePath + "/logoandcover";
            case SERVICE -> basePath + "/photos";
        };
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoDTO> getPhotosByType(UUID carWashId, PhotoType type) {
        CarWashProfile profile = getProfileByCarWashId(carWashId);
        Hibernate.initialize(profile.getPhotos());

        switch (type) {
            case LOGO:
                return profile.getLogo() != null ?
                        List.of(new PhotoDTO(buildFullUrl(profile.getLogo()))) : List.of();
            case COVER:
                return profile.getCoverPhoto() != null ?
                        List.of(new PhotoDTO(buildFullUrl(profile.getCoverPhoto()))) : List.of();
            case SERVICE:
                return profile.getPhotos().stream()
                        .map(this::buildFullUrl)
                        .map(PhotoDTO::new)
                        .collect(Collectors.toList());
            default:
                return List.of();
        }
    }

    @Transactional(readOnly = true)
    public List<OptimizedPhotoResponse> getServicePhotosOptimized(UUID carWashId) {
        CarWashProfile profile = getProfileByCarWashId(carWashId);
        Hibernate.initialize(profile.getPhotos());

        return profile.getPhotos().stream()
                .map(originalRelativePath -> {
                    String thumbRelativePath;

                    if (originalRelativePath != null && originalRelativePath.contains("_original")) {
                        thumbRelativePath = generateThumbnailPath(originalRelativePath);
                    } else {
                        thumbRelativePath = originalRelativePath;
                    }

                    return new OptimizedPhotoResponse(
                            buildFullUrl(originalRelativePath),
                            buildFullUrl(thumbRelativePath),
                            "Foto do serviço"
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removePhoto(UUID carWashId, String filenameFromRequest) {
        CarWashProfile profile = getProfileByCarWashId(carWashId);
        Hibernate.initialize(profile.getPhotos());

        String originalPathToRemove = profile.getPhotos().stream()
                .filter(dbPath -> dbPath.endsWith(filenameFromRequest))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found for filename: " + filenameFromRequest));

        if (originalPathToRemove.contains("_original")) {
            String thumbnailPathToDelete = generateThumbnailPath(originalPathToRemove);
            storageService.delete(thumbnailPathToDelete);
        }

        profile.getPhotos().remove(originalPathToRemove);
        storageService.delete(originalPathToRemove);

        profileRepository.save(profile);
    }

    private String generateThumbnailPath(String originalPath) {
        if (originalPath == null) return null;
        int lastDot = originalPath.lastIndexOf('.');
        if (lastDot == -1) return originalPath;
        String baseName = originalPath.substring(0, lastDot);
        return baseName.replace("_original", "_thumb") + ".webp";
    }

    private CarWashProfile getProfileByCarWashId(UUID profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for ID: " + profileId));
    }

    private String buildFullUrl(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return null;
        }
        return storageProperties.getBaseUrl() + "/uploads/" + relativePath;
    }
}
