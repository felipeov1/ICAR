package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.application.service.carwash.profile.PhotoService;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final CarWashProfileRepository profileRepository;

    @Override
    @Transactional
    public void addPhotos(UUID carWashId, List<String> photos) {
        CarWashProfile profile = profileRepository.findByCarWashId(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));
        profile.getPhotos().addAll(photos);
        profileRepository.save(profile);
    }

    @Override
    @Transactional
    public void removePhoto(UUID carWashId, String photoUrl) {
        CarWashProfile profile = profileRepository.findByCarWashId(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));
        profile.getPhotos().remove(photoUrl);
        profileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getPhotos(UUID carWashId) {
        return profileRepository.findByCarWashId(carWashId)
                .map(CarWashProfile::getPhotos)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));
    }
}