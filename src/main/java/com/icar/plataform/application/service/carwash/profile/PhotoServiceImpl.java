package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.PhotoDTO;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.plataform.infrastructure.storage.StorageService;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final CarWashProfileRepository profileRepository;
    private final StorageService storageService;

    @Override
    @Transactional
    public void addPhotos(UUID carWashId, List<MultipartFile> files) {
        CarWashProfile profile = profileRepository.findByCarWashRegistration_Id(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        List<String> savedPhotoUrls = files.stream()
                .map(file -> storageService.store(file, "carwash/" + carWashId + "/photos"))
                .toList();

        profile.getPhotos().addAll(savedPhotoUrls);
        profileRepository.save(profile);
    }

    @Override
    @Transactional
    public void removePhoto(UUID carWashId, String photoName) {
        CarWashProfile profile = profileRepository.findByCarWashRegistration_Id(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Hibernate.initialize(profile.getPhotos());

        String fullPhotoUrlPrefix = "uploads/carwash/" + carWashId + "/photos/";

        String fullPhotoUrl = fullPhotoUrlPrefix + photoName;

        boolean removed = profile.getPhotos().remove(fullPhotoUrl);
        if (!removed) {
            throw new ResourceNotFoundException("Photo URL not found in profile");
        }

        storageService.delete(fullPhotoUrl);
        profileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoDTO> getPhotos(UUID carWashId) {
        CarWashProfile profile = profileRepository.findByCarWashRegistration_Id(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Hibernate.initialize(profile.getPhotos());

        return profile.getPhotos().stream()
                .map(PhotoDTO::new)
                .collect(Collectors.toList());
    }
}