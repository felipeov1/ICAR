package com.icar.plataform.api.dto.request.carwash;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarWashProfileRequest {

    private String name;
    private String description;
    private String coverPhoto;
    private List<String> photos;
    private List<String> openingHours;
    private List<String> vehicleTypes;
}