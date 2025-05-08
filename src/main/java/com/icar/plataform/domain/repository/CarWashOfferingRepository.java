package com.icar.plataform.domain.repository;

import com.icar.plataform.domain.model.CarWash;
import com.icar.plataform.domain.model.CarWashOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarWashOfferingRepository extends JpaRepository<CarWashOffering, UUID> {
    List<CarWashOffering> findByCarWashAndActiveTrue(CarWash carWash);
    List<CarWashOffering> findByCarWashId(UUID carWashId);
    boolean existsByCarWashAndNameIgnoreCase(CarWash carWash, String name);
}
