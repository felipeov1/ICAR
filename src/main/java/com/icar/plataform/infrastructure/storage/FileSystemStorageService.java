// Caminho: src/main/java/com/icar/plataform/infrastructure/storage/FileSystemStorageService.java
package com.icar.plataform.infrastructure.storage;

import com.icar.plataform.infrastructure.storage.config.StorageProperties;
import com.icar.plataform.infrastructure.storage.exception.StorageException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

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
            throw new StorageException("Não foi possível inicializar o diretório de armazenamento.", e);
        }
    }

    @Override
    public String store(MultipartFile file, String relativePath) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Arquivo vazio: " + filename);
            }

            Path destinationFolder = rootLocation.resolve(relativePath);
            Files.createDirectories(destinationFolder);

            Path destinationFile = destinationFolder.resolve(filename).normalize();
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return Paths.get(properties.getLocation(), relativePath, filename).toString().replace("\\", "/");

        } catch (IOException e) {
            throw new StorageException("Erro ao armazenar arquivo: " + filename, e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            Path filePath = Paths.get(path).toAbsolutePath();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new StorageException("Erro ao excluir arquivo: " + path, e);
        }
    }
}
