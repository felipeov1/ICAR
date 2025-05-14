package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashScheduleConfigRequest;
import com.icar.plataform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.plataform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.plataform.api.mapper.carwash.ScheduleMapper;
import com.icar.plataform.application.service.carwash.profile.ScheduleService;
import com.icar.plataform.domain.model.carwash.profile.*;
import com.icar.plataform.domain.repository.carwash.profile.*;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final CarWashProfileRepository profileRepository;
    private final WeeklyScheduleRepository weeklyScheduleRepository;
    private final SpecialDayRepository specialDayRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    @Transactional
    public void setScheduleConfiguration(UUID carWashId, CarWashScheduleConfigRequest request) {
        CarWashProfile profile = profileRepository.findByCarWashId(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));

        updateWeeklySchedules(profile, request.getWeeklySchedules());
        updateSpecialDays(profile, request.getSpecialDays());
    }

    private void updateWeeklySchedules(CarWashProfile profile, List<WeeklyScheduleRequest> weeklySchedules) {
        weeklyScheduleRepository.deleteByProfileId(profile.getId());
        weeklySchedules.forEach(ws -> {
            WeeklySchedule schedule = scheduleMapper.toEntity(ws);
            schedule.setProfile(profile);
            weeklyScheduleRepository.save(schedule);
        });
    }

    private void updateSpecialDays(CarWashProfile profile, List<SpecialDayRequest> specialDays) {
        specialDayRepository.deleteByProfileId(profile.getId());
        specialDays.forEach(sd -> {
            SpecialDay specialDay = scheduleMapper.toEntity(sd);
            specialDay.setProfile(profile);
            specialDayRepository.save(specialDay);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDateTime> getAvailableSlots(UUID carWashId, LocalDate date) {
        CarWashProfile profile = profileRepository.findByCarWashId(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));

        // Verifica se existe um dia especial para a data
        SpecialDay specialDay = profile.getSpecialDays().stream()
                .filter(sd -> sd.getDate().equals(date))
                .findFirst()
                .orElse(null);

        LocalTime startTime;
        LocalTime endTime;
        int intervalMinutes;

        if (specialDay != null) {
            startTime = specialDay.getStartTime();
            endTime = specialDay.getEndTime();
            intervalMinutes = 30; // define um valor padrão fixo para feriados ou dias especiais
        } else {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            WeeklySchedule schedule = profile.getWeeklySchedules().stream()
                    .filter(ws -> ws.getDayOfWeek().equals(dayOfWeek) && ws.isAvailable())
                    .findFirst()
                    .orElse(null);

            if (schedule == null) {
                return Collections.emptyList(); // Não há expediente neste dia
            }

            startTime = schedule.getStartTime();
            endTime = schedule.getEndTime();
            intervalMinutes = schedule.getAppointmentIntervalMinutes();
        }

        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime slot = date.atTime(startTime);

        while (!slot.toLocalTime().isAfter(endTime.minusMinutes(intervalMinutes))) {
            slots.add(slot);
            slot = slot.plusMinutes(intervalMinutes);
        }

        return slots;
    }
}
