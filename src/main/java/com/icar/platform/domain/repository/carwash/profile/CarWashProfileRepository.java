package com.icar.platform.domain.repository.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface CarWashProfileRepository extends JpaRepository<CarWashProfile, UUID> {

    @Override
    @NonNull
    Optional<CarWashProfile> findById(@NonNull UUID profileId);

    @Override
    boolean existsById(@NonNull UUID profileId);

    boolean existsBySubdomain(String subdomain);

    Optional<CarWashProfile> findByCarWashRegistrationId(UUID carWashId);

    @Query("SELECT p.id FROM CarWashProfile p WHERE p.carWashRegistration.id = :carWashId")
    Optional<UUID> findProfileIdByRegistrationId(@NonNull @Param("carWashId") UUID carWashId);

    Optional<CarWashProfile> findBySubdomain(String subdomain);
}
