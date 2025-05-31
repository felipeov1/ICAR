// Caminho: src/main/java/com/icar/plataform/infrastructure/storage/StorageService.java
package com.icar.plataform.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String relativePath);
    void delete(String path);
}
