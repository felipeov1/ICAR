package com.icar.plataform.domain.repository.carwash.profile;

import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.model.carwash.profile.WeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, UUID> {
    List<WeeklySchedule> findByProfile_CarWashRegistration_IdAndDayOfWeek(UUID carWashId, DayOfWeek dayOfWeek);
    Optional<WeeklySchedule> findByIdAndProfile_CarWashRegistration_Id(UUID id, UUID carWashId);
    Optional<WeeklySchedule> findByProfileAndDayOfWeek(CarWashProfile profile, DayOfWeek dayOfWeek);
    void deleteByProfile_CarWashRegistration_Id(UUID carWashId);
    List<WeeklySchedule>findByProfile_CarWashRegistration_Id(UUID carWashid);
}

