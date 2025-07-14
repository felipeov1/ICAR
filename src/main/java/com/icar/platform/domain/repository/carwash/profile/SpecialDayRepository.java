package com.icar.platform.domain.repository.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.SpecialDay;
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

    @Query("SELECT sd FROM SpecialDay sd WHERE sd.id = :id AND sd.profile.carWashRegistration.id = :carWashId")
    Optional<SpecialDay> findByIdAndProfile_CarWashRegistration_IdIncludingInactive(UUID id, UUID carWashId);

    @Modifying
    @Query("UPDATE SpecialDay sd SET sd.active = :active WHERE sd.id = :id AND sd.profile.carWashRegistration.id = :carWashId")
    void updateActiveStatus(UUID id, UUID carWashId, boolean active);
        List<SpecialDay> findByProfile_Id(UUID profileId);
        List<SpecialDay> findByProfile_IdAndActive(UUID profileId, boolean active);
        List<SpecialDay> findByProfile_IdAndDateBetween(UUID profileId, LocalDate startDate, LocalDate endDate);
        List<SpecialDay> findByProfile_IdAndDateBetweenAndActive(UUID profileId, LocalDate startDate, LocalDate endDate, boolean active);
        Optional<SpecialDay> findByProfile_IdAndDateAndActive(UUID profileId, LocalDate date, boolean active);

}