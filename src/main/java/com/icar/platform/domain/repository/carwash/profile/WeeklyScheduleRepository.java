package com.icar.platform.domain.repository.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.WeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, UUID> {
    List<WeeklySchedule> findByProfile_Id(UUID profileId);
    Optional<WeeklySchedule> findByIdAndProfile_Id(UUID id, UUID profileId);
    void deleteByProfile_Id(UUID profileId);
    Optional<WeeklySchedule> findByProfile_IdAndDayOfWeek(UUID profileId, DayOfWeek dayOfWeek);
}

