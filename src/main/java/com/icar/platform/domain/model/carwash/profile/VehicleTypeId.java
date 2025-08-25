package com.icar.platform.domain.model.carwash.profile;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class VehicleTypeId implements Serializable {
    private UUID profileId;
    private String vehicleType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleTypeId that = (VehicleTypeId) o;
        return Objects.equals(profileId, that.profileId) && Objects.equals(vehicleType, that.vehicleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, vehicleType);
    }
}