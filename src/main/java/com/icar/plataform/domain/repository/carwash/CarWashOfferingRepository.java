package com.icar.plataform.domain.repository.carwash;

import com.icar.plataform.domain.model.carwash.CarWashProfile;
import com.icar.plataform.domain.model.carwash.CarWashOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarWashOfferingRepository extends JpaRepository<CarWashOffering, UUID> {

    boolean existsByCarWashProfileAndNameIgnoreCase(CarWashProfile carWashProfile, String name);

    List<CarWashOffering> findByCarWashProfileId(UUID carWashProfileId);
}
