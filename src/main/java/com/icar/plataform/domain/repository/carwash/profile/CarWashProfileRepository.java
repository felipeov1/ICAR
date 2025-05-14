package com.icar.plataform.domain.repository.carwash.profile;

import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarWashProfileRepository extends JpaRepository<CarWashProfile, UUID> {
    Optional<CarWashProfile> findByCarWashId(UUID carWashId);
}