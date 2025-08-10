package com.icar.platform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    private String observations;
}