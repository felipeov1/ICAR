package com.icar.platform.api.dto.response.ads;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
}