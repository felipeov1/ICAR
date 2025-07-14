package com.icar.platform.infrastructure.storage;

import com.icar.platform.infrastructure.storage.config.StorageProperties;
import com.icar.platform.infrastructure.storage.exception.StorageException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
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

    @Override
    public String store(MultipartFile file, String relativePath) {
        try {
            String filename = generateUniqueFilename(file.getOriginalFilename());
            Path targetLocation = this.rootLocation.resolve(relativePath).resolve(filename);

            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return Paths.get(relativePath, filename).toString().replace("\\", "/");
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
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