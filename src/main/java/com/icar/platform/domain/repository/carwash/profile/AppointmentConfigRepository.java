package com.icar.platform.domain.repository.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppointmentConfigRepository extends JpaRepository<AppointmentConfig, UUID> {
    Optional<AppointmentConfig> findByProfile_Id(UUID profileId);
    boolean existsByProfile_CarWashRegistration_Id(UUID carWashId);
}