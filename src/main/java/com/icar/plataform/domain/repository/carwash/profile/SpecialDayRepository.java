package com.icar.plataform.domain.repository.carwash.profile;

import com.icar.plataform.domain.model.carwash.profile.SpecialDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecialDayRepository extends JpaRepository<SpecialDay, UUID> {

    Optional<SpecialDay> findByIdAndProfile_CarWashRegistration_Id(UUID id, UUID carWashId);

    Optional<SpecialDay> findByProfile_CarWashRegistration_IdAndDateAndActive(UUID carWashId, LocalDate date, boolean active);

    List<SpecialDay> findByProfile_CarWashRegistration_IdAndActive(UUID carWashId, boolean active);

    List<SpecialDay> findByProfile_CarWashRegistration_Id(UUID carWashId);

    List<SpecialDay> findByProfile_CarWashRegistration_IdAndDateBetween(UUID carWashId, LocalDate startDate, LocalDate endDate);

    List<SpecialDay> findByProfile_CarWashRegistration_IdAndDateBetweenAndActive(
            UUID carWashId, LocalDate startDate, LocalDate endDate, boolean active);

    @Query("SELECT sd FROM SpecialDay sd WHERE sd.id = :id AND sd.profile.carWashRegistration.id = :carWashId")
    Optional<SpecialDay> findByIdAndProfile_CarWashRegistration_IdIncludingInactive(UUID id, UUID carWashId);

    @Modifying
    @Query("UPDATE SpecialDay sd SET sd.active = :active WHERE sd.id = :id AND sd.profile.carWashRegistration.id = :carWashId")
    void updateActiveStatus(UUID id, UUID carWashId, boolean active);
}