package com.icar.platform.domain.repository.carwash.offering;

import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CarWashOfferingRepository extends JpaRepository<CarWashOffering, UUID> {
    List<CarWashOffering> findByProfile_Id(UUID profileId);

    @Query("SELECT COUNT(o) > 0 FROM CarWashOffering o WHERE o.profile.id = :profileId AND LOWER(o.name) = LOWER(:name)")
    boolean existsByProfileIdAndNameIgnoreCase(
            @Param("profileId") UUID profileId,
            @Param("name") String name
    );

}