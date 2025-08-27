package com.icar.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absoluteUploadsPath = "file:///var/www/icarplus/uploads/";

        registry.addResourceHandler("/uploads/static/**")
                .addResourceLocations(absoluteUploadsPath + "static/");

        registry.addResourceHandler("/uploads/carwash/**")
                .addResourceLocations(absoluteUploadsPath + "carwash/");
    }
}