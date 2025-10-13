package com.icar.platform.api.dto.response.carwash.profile;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class OptimizedPhotoResponse {
    private String originalUrl;
    private String thumbnailUrl;
    private String altText;
}