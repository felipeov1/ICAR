package com.icar.plataform.domain.repository.carwash.legal;

import com.icar.plataform.domain.model.carwash.legal.CarWashRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarWashRegistrationDataRepository extends JpaRepository<CarWashRegistration, UUID> {

    List<CarWashRegistration> findAllByOrderByCreatedAtAsc();

}