package com.icar.plataform.domain.repository.carwash;

import com.icar.plataform.domain.model.carwash.CarWashProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarWashProfileRepository extends JpaRepository<CarWashProfile, UUID> {
    @EntityGraph(attributePaths = {"photos", "openingHours", "vehicleTypes"})
    Optional<CarWashProfile> findByCarWashId(UUID carWashId);
}
