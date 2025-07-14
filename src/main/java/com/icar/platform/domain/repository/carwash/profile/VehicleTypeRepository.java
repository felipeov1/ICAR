package com.icar.platform.domain.repository.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.VehicleType;
import com.icar.platform.domain.model.carwash.profile.VehicleTypeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, VehicleTypeId> {

    @Query("SELECT vt.vehicleType FROM VehicleType vt WHERE vt.profileId = :profileId")
    List<String> findVehicleTypesByProfileId(@Param("profileId") UUID profileId);

    @Modifying
    @Query("DELETE FROM VehicleType vt WHERE vt.profileId = :profileId")
    void deleteByProfileId(@Param("profileId") UUID profileId);
}