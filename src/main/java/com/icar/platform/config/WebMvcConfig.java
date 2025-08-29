package com.icar.platform.config;

import com.icar.platform.infrastructure.storage.config.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final StorageProperties storageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Obtenha o caminho de armazenamento do seu StorageProperties
        String uploadPath = "file:" + storageProperties.getLocation() + "/";

        // Mapeie a URL "/uploads/**" para o diretório de uploads configurado
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}