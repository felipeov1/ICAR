package com.icar.plataform.domain.repository.carwash.offering;

import com.icar.plataform.domain.model.carwash.offering.CarWashOffering;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarWashOfferingRepository extends JpaRepository<CarWashOffering, UUID> {

    @Query("SELECT o FROM CarWashOffering o WHERE o.profile.carWashRegistration.id = :carWashId AND o.deletedAt IS NULL")
    List<CarWashOffering> findByCarWashIdAndDeletedAtIsNull(UUID carWashId);

    @Query("SELECT o FROM CarWashOffering o WHERE o.id = :id AND o.deletedAt IS NULL")
    Optional<CarWashOffering> findByIdAndDeletedAtIsNull(UUID id);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM CarWashOffering o WHERE o.id = :id AND o.deletedAt IS NULL")
    boolean existsByIdAndDeletedAtIsNull(UUID id);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
            "FROM CarWashOffering o " +
            "WHERE o.profile.carWashRegistration.id = :carWashId " +
            "AND LOWER(o.name) = LOWER(:name) " +
            "AND o.deletedAt IS NULL")
    boolean existsByCarWashIdAndNameIgnoreCaseAndDeletedAtIsNull(UUID carWashId, String name);

    @Modifying
    @Query("UPDATE CarWashOffering o SET o.deletedAt = CURRENT_TIMESTAMP WHERE o.id = :id")
    int softDeleteById(UUID id);  // Retorna o número de linhas afetadas

    @Modifying
    @Query("UPDATE CarWashOffering o SET o.active = :active WHERE o.id = :id")
    void updateActiveStatus(UUID id, boolean active);
}