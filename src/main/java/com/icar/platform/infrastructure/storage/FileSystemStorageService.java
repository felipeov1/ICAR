package com.icar.platform.infrastructure.storage;

import com.icar.platform.infrastructure.storage.config.StorageProperties;
import com.icar.platform.infrastructure.storage.exception.StorageException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements StorageService {

    private final StorageProperties properties;
    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage directory", e);
        }
    }

    public Map<String, String> storeAndCreateThumbnail(MultipartFile file, String relativePath) {
        try {
            String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String extension = StringUtils.getFilenameExtension(originalFilename);
            String baseName = UUID.randomUUID().toString();

            String originalFileameWithUUID = baseName + "_original." + extension;
            String thumbnailFilename = baseName + "_thumb.webp";

            Path originalLocation = this.rootLocation.resolve(relativePath).resolve(originalFileameWithUUID);
            Path thumbnailLocation = this.rootLocation.resolve(relativePath).resolve(thumbnailFilename);

            Files.createDirectories(originalLocation.getParent());

            Files.copy(file.getInputStream(), originalLocation, StandardCopyOption.REPLACE_EXISTING);

            ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(400, 400)
                    .outputFormat("webp")
                    .outputQuality(0.70)
                    .toOutputStream(thumbOutput);
            Files.copy(new ByteArrayInputStream(thumbOutput.toByteArray()), thumbnailLocation);

            Map<String, String> paths = new HashMap<>();
            paths.put("original", Paths.get(relativePath, originalFileameWithUUID).toString().replace("\\", "/"));
            paths.put("thumbnail", Paths.get(relativePath, thumbnailFilename).toString().replace("\\", "/"));

            return paths;

        } catch (IOException e) {
            throw new StorageException("Failed to store file and create thumbnail", e);
        }
    }

    @Override
    public String store(MultipartFile file, String relativePath) {
        return storeAndCreateThumbnail(file, relativePath).get("original");
    }

    @Override
    public void delete(String filePath) {
        try {
            Path fileToDelete = this.rootLocation.resolve(filePath).normalize();
            if (!Files.exists(fileToDelete)) {
                throw new StorageException("File not found: " + filePath);
            }
            Files.delete(fileToDelete);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file", e);
        }
    }

    private String generateUniqueFilename(String originalFilename) {
        return UUID.randomUUID() + "_" + StringUtils.cleanPath(originalFilename);
    }
}