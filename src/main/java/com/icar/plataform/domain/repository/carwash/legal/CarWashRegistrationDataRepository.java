package com.icar.plataform.domain.repository.carwash.legal;

import com.icar.plataform.domain.model.carwash.legal.CarWashRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarWashRegistrationDataRepository extends JpaRepository<CarWashRegistration, UUID> {

    boolean existsBySubdomainAndDeletedAtIsNull(String subdomain);

    List<CarWashRegistration> findAllByDeletedAtIsNullOrderByCreatedAtAsc();

    Optional<CarWashRegistration> findByIdAndDeletedAtIsNull(UUID id);

    Optional<CarWashRegistration> findBySubdomainAndDeletedAtIsNull(String subdomain);
}
