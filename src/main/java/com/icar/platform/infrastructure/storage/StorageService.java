package com.icar.platform.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String relativePath);
    void delete(String path);
}