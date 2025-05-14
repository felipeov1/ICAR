package com.icar.plataform.domain.repository.carwash.offering;

import com.icar.plataform.domain.model.carwash.offering.CarWashOffering;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarWashOfferingRepository extends JpaRepository<CarWashOffering, UUID> {

    boolean existsByProfileAndNameIgnoreCase(CarWashProfile profile, String name);

    List<CarWashOffering> findByProfileId(UUID carWashProfileId);
}
