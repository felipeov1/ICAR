package com.icar.platform.api.dto.response.ads;

import com.icar.platform.api.dto.response.admin.PartnerSummaryResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AdvertisementResponse {
    private UUID id;
    private String title;
    private String description;
    private String imageUrl;
    private String link;

    private PartnerSummaryResponse partner;
    private String status;
    private LocalDateTime expiresAt;
    private LocalDateTime startDate;
}