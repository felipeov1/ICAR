package com.icar.platform.api.dto.request.ads;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AdvertisementRequest {
    private UUID carWashProfileId;
    private String title;
    private String description;
    private String linkUrl;
    private LocalDateTime expiresAt;
    private boolean isActive = true;
    private boolean isPlatformAd = false;
}