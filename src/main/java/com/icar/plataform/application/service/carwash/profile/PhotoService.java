package com.icar.plataform.application.service.carwash.profile;

import java.util.List;
import java.util.UUID;

public interface PhotoService {
    void addPhotos(UUID carWashId, List<String> photos);
    void removePhoto(UUID carWashId, String photoUrl);
    List<String> getPhotos(UUID carWashId);
}

