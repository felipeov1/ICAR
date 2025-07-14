package com.icar.platform.api.dto.request.carwash.profile;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarWashProfileRequest {
    private String name;
    private String description;
    private String logo;
    private String coverPhoto;
    private String[] modalities;
    private String priceRange;
}