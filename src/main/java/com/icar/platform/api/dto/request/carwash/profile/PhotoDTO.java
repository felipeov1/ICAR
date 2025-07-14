package com.icar.platform.api.dto.request.carwash.profile;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PhotoDTO {
    private String photoUrl;
    private LocalDateTime uploadedAt;
    public PhotoDTO(String photoUrl) {
        this.photoUrl = photoUrl;
        this.uploadedAt = LocalDateTime.now();
    }
}