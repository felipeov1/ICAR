package com.icar.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${storage.location}")
    private String storageLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPathUri = Paths.get(storageLocation).toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/uploads/static/**")
                .addResourceLocations(uploadPathUri + "static/");

        registry.addResourceHandler("/uploads/carwash/**")
                .addResourceLocations(uploadPathUri + "carwash/");
    }
}