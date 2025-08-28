package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.PhotoDTO;
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

        List<PhotoServicesResponse> responses = files.stream()
                .map(file -> {
                    String photoUrl = storageService.store(file, getPhotoPath(carWashId, PhotoType.SERVICE));
                    profile.getPhotos().add(photoUrl);
                    return new PhotoServicesResponse(
                            photoUrl,
                            "Service photo uploaded successfully",
                            LocalDateTime.now()
                    );
                })
                .toList();

        profileRepository.save(profile);
        return responses;
    }

    // Corrected code
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
        String photoUrl = storageService.store(file, getPhotoPath(carWashId, type));

        switch (type) {
            case LOGO:
                if (profile.getLogo() != null) {
                    storageService.delete(profile.getLogo());
                }
                profile.setLogo(photoUrl);
                break;
            case COVER:
                if (profile.getCoverPhoto() != null) {
                    storageService.delete(profile.getCoverPhoto());
                }
                profile.setCoverPhoto(photoUrl);
                break;
            case SERVICE:
                profile.getPhotos().add(photoUrl);
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

    @Override
    @Transactional
    public void removePhoto(UUID carWashId, String photoUrlFromRequest) {
        CarWashProfile profile = getProfileByCarWashId(carWashId);
        Hibernate.initialize(profile.getPhotos());

        String filenameFromRequest = photoUrlFromRequest.substring(photoUrlFromRequest.lastIndexOf('/') + 1);

        java.util.Optional<String> fullPathToRemove = profile.getPhotos().stream()
                .filter(dbPath -> dbPath.endsWith(filenameFromRequest))
                .findFirst();
        if (fullPathToRemove.isPresent()) {
            String path = fullPathToRemove.get();
            profile.getPhotos().remove(path);
            storageService.delete(path);
        } else {
            throw new ResourceNotFoundException("Photo not found in profile's list for filename: " + filenameFromRequest);
        }

        profileRepository.save(profile);
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