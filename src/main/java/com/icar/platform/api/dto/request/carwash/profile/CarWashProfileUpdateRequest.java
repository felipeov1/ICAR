package com.icar.platform.api.dto.request.carwash.profile;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CarWashProfileUpdateRequest {

    private String name;
    private String description;
    private String whatsapp;
    private List<String> locations;
    private List<String> modalities;
    private String wetWashObservations;
    private String dryWashObservations;
}