package com.icar.platform.api.dto.response.carwash.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTypesResponse {
    private UUID profileId;
    private List<String> vehicleTypes;
}