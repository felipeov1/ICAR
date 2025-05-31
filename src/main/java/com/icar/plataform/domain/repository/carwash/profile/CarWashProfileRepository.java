package com.icar.plataform.domain.repository.carwash.profile;

import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarWashProfileRepository extends JpaRepository<CarWashProfile, UUID> {
    Optional<CarWashProfile> findByCarWashRegistration_Id(UUID carWashId);
    boolean existsByCarWashRegistration_Id(UUID carWashId);
}