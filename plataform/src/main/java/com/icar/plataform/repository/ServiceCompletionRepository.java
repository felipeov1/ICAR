package com.icar.plataform.repository;

import com.icar.plataform.domain.model.ServiceCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceCompletionRepository extends JpaRepository<ServiceCompletion, UUID> {
    Optional<ServiceCompletion> findByAppointmentId(UUID appointmentId);
}
