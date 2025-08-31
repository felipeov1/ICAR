package com.icar.platform.domain.repository.carwash.legal;

import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarWashRegistrationDataRepository extends JpaRepository<CarWashRegistration, UUID> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"profile"})
    List<CarWashRegistration> findAll();

    @EntityGraph(attributePaths = {"profile"})
    List<CarWashRegistration> findAllByDeletedAtIsNullOrderByCreatedAtAsc();

    @EntityGraph(attributePaths = {"profile"})
    Optional<CarWashRegistration> findByIdAndDeletedAtIsNull(UUID id);

    Optional<CarWashRegistration> findByEmail(String email);

    Optional<CarWashRegistration> findByCnpj(String cnpj);
    Optional<CarWashRegistration> findByCpf(String cpf);
}