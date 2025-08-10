package com.icar.platform.domain.repository.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.SpecialDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecialDayRepository extends JpaRepository<SpecialDay, UUID> {
    @Query("SELECT sd FROM SpecialDay sd WHERE sd.id = :id AND sd.profile.id = :profileId")
    Optional<SpecialDay> findByIdAndProfileIdIncludeDeleted(@Param("id") UUID id, @Param("profileId") UUID profileId);

    boolean existsByIdAndProfile_Id(UUID id, UUID profileId);

    @Query("SELECT sd FROM SpecialDay sd WHERE sd.profile.id = :profileId AND sd.startDate <= :date AND sd.endDate >= :date ORDER BY sd.createdAt DESC")
    List<SpecialDay> findActiveByProfileIdAndDate(@Param("profileId") UUID profileId, @Param("date") LocalDate date);

    List<SpecialDay> findByProfile_IdAndEndDateGreaterThanEqual(@Param("profileId") UUID profileId, @Param("date") LocalDate date);

    @Query("SELECT sd FROM SpecialDay sd WHERE sd.profile.id = :profileId " +
            "AND sd.id <> :excludeId " +
            "AND sd.startDate <= :endDate AND sd.endDate >= :startDate")
    List<SpecialDay> findOverlappingRanges(
            @Param("profileId") UUID profileId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("excludeId") UUID excludeId
    );
}