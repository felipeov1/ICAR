package com.icar.platform.domain.model.carwash.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "car_wash_profile_vehicle_types")
@IdClass(VehicleTypeId.class)
public class VehicleType {

    @Id
    @Column(name = "profile_id", nullable = false)
    private UUID profileId;

    @Id
    @Column(name = "vehicle_type", nullable = false, length = 100)
    private String vehicleType;
}