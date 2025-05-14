package com.icar.plataform.domain.repository.carwash.profile;

import com.icar.plataform.domain.model.carwash.profile.SpecialDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecialDayRepository extends JpaRepository<SpecialDay, UUID> {
    Optional<SpecialDay> findByProfileIdAndDate(UUID profileId, LocalDate date);
    void deleteByProfileId(UUID profileId);
}
