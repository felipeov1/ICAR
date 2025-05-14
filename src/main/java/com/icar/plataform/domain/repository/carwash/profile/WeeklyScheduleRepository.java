package com.icar.plataform.domain.repository.carwash.profile;

import com.icar.plataform.domain.model.carwash.profile.WeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, UUID> {
    Optional<WeeklySchedule> findByProfileIdAndDayOfWeek(UUID profileId, DayOfWeek dayOfWeek);
    void deleteByProfileId(UUID profileId);
}
