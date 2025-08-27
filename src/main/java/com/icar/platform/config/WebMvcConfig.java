package com.icar.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPathUri = Paths.get("uploads").toAbsolutePath().toUri().toString();


        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPathUri);

        registry.addResourceHandler("/carwash/**")
                .addResourceLocations(uploadPathUri + "carwash/");
    }
}