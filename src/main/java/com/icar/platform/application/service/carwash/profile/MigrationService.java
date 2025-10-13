package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.infrastructure.storage.config.StorageProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor; // 1. Adicionado import do Lombok
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 2. Corrigido o import do Transactional
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // 3. Adicionada anotação para injeção de dependência via construtor
public class MigrationService {

    private final CarWashProfileRepository profileRepository;
    private final StorageProperties properties;
    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath().normalize();
    }

    @Transactional
    public void migrateExistingPhotos() {
        List<CarWashProfile> profiles = profileRepository.findAll();

        for (CarWashProfile profile : profiles) {
            List<String> updatedPaths = new ArrayList<>();
            List<String> photosToMigrate = new ArrayList<>(profile.getPhotos()); // Evita ConcurrentModificationException

            for (String oldPath : photosToMigrate) {
                if (oldPath.contains("_original")) {
                    updatedPaths.add(oldPath);
                    continue;
                }

                try {
                    Path oldFilePath = rootLocation.resolve(oldPath);
                    if (!Files.exists(oldFilePath)) {
                        System.err.println("Arquivo não encontrado, pulando migração: " + oldPath);
                        continue;
                    }

                    String originalFilename = oldFilePath.getFileName().toString();
                    String extension = StringUtils.getFilenameExtension(originalFilename);
                    String baseName = UUID.randomUUID().toString();

                    String newOriginalFilename = baseName + "_original." + extension;
                    String newThumbnailFilename = baseName + "_thumb.webp";
                    Path newOriginalFilePath = oldFilePath.resolveSibling(newOriginalFilename);
                    Path newThumbnailFilePath = oldFilePath.resolveSibling(newThumbnailFilename);

                    Files.move(oldFilePath, newOriginalFilePath, StandardCopyOption.REPLACE_EXISTING);

                    Thumbnails.of(newOriginalFilePath.toFile())
                            .size(400, 400)
                            .outputFormat("webp")
                            .outputQuality(0.85)
                            .toFile(newThumbnailFilePath.toFile());

                    String dbPath = oldPath.contains("/") ?
                            Paths.get(oldPath).getParent().resolve(newOriginalFilename).toString().replace("\\", "/") :
                            newOriginalFilename;
                    updatedPaths.add(dbPath);

                } catch (IOException e) {
                    System.err.println("Falha ao migrar arquivo: " + oldPath + " | Erro: " + e.getMessage());
                }
            }
            profile.getPhotos().clear();
            profile.getPhotos().addAll(updatedPaths);
            profileRepository.save(profile);
        }
    }
}
