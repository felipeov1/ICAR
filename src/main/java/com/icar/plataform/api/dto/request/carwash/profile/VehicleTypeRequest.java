package com.icar.plataform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VehicleTypeRequest {
    @NotEmpty
    private List<String> vehicleTypes;
}
