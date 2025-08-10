package com.icar.platform.api.dto.response.carwash.profile;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoServicesResponse {
    private String url;
    private String message;
    private LocalDateTime uploadedAt;
}