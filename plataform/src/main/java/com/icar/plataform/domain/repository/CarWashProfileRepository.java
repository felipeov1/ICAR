package com.icar.plataform.domain.repository;

import com.icar.plataform.domain.model.CarWashProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarWashProfileRepository extends JpaRepository<CarWashProfile, UUID> {
    CarWashProfile findByCarWashId(UUID carWashId);
}
