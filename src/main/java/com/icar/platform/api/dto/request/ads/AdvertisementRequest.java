package com.icar.platform.api.dto.request.ads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AdvertisementRequest {
    @NotNull
    private UUID carWashProfileId;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String linkUrl;
    @NotNull
    @Future
    private LocalDateTime expiresAt;
    private boolean isActive = true;
    private boolean isPlatformAd = false;
}