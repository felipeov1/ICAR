package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.PhotoDTO;
import com.icar.platform.api.dto.response.carwash.profile.OptimizedPhotoResponse;
import com.icar.platform.api.dto.response.carwash.profile.PhotoServicesResponse;
import com.icar.platform.domain.enums.PhotoType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PhotoService {

    void addPhoto(UUID carWashId, MultipartFile file, PhotoType type);

    void removePhoto(UUID carWashId, String photoUrl);

    List<PhotoServicesResponse> addServicePhotos(UUID carWashId, List<MultipartFile> files);
    List<PhotoServicesResponse> getServicePhotos(UUID carWashId);

    List<PhotoDTO> getPhotosByType(UUID carWashId, PhotoType type);
    List<OptimizedPhotoResponse> getServicePhotosOptimized(UUID carWashId);


}