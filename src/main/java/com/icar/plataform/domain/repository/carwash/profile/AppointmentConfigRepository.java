package com.icar.plataform.domain.repository.carwash.profile;

import com.icar.plataform.domain.model.carwash.profile.AppointmentConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppointmentConfigRepository extends JpaRepository<AppointmentConfig, UUID> {
    Optional<AppointmentConfig> findByProfile_CarWashRegistration_Id(UUID carWashId);
    boolean existsByProfile_CarWashRegistration_Id(UUID carWashId);
}