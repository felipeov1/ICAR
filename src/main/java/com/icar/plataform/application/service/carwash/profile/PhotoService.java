package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.PhotoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PhotoService {
    void addPhotos(UUID carWashId, List<MultipartFile> files);
    void removePhoto(UUID carWashId, String photoUrl);
    List<PhotoDTO> getPhotos(UUID carWashId);
}