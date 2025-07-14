package com.icar.platform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfilePhotoRequest {
    @NotEmpty
    private List<String> photos;
}