package com.icar.plataform.application.service.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.plataform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.plataform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.plataform.api.dto.response.carwash.profile.TimeSlotResponse;
import com.icar.plataform.api.dto.response.carwash.profile.WeeklyScheduleResponse;
import com.icar.plataform.api.mapper.carwash.ScheduleMapper;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.model.carwash.profile.SpecialDay;
import com.icar.plataform.domain.model.carwash.profile.WeeklySchedule;
import com.icar.plataform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.plataform.domain.repository.carwash.profile.SpecialDayRepository;
import com.icar.plataform.domain.repository.carwash.profile.WeeklyScheduleRepository;
import com.icar.plataform.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleManagementServiceImpl implements ScheduleManagementService {

    private final CarWashProfileRepository profileRepository;
    private final WeeklyScheduleRepository weeklyScheduleRepository;
    private final SpecialDayRepository specialDayRepository;
    private final ScheduleMapper mapper;

    // Weekly schedules implementation
    @Override
    public List<WeeklyScheduleResponse> getAllWeeklySchedules(UUID carWashId) {
        List<WeeklySchedule> schedules = weeklyScheduleRepository.findByProfile_CarWashRegistration_Id(carWashId);

        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException("No weekly schedules found for car wash with id: " + carWashId);
        }

        return schedules.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public WeeklyScheduleResponse getWeeklySchedule(UUID carWashId, UUID scheduleId) {
        return weeklyScheduleRepository.findByIdAndProfile_CarWashRegistration_Id(scheduleId, carWashId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Weekly schedule not found"));
    }

    @Override
    @Transactional
    public List<WeeklyScheduleResponse> createWeeklySchedules(UUID carWashId, List<WeeklyScheduleRequest> requests) {
        CarWashProfile profile = getProfile(carWashId);
        return requests.stream()
                .map(request -> {
                    WeeklySchedule schedule = mapper.toEntity(request);
                    schedule.setProfile(profile);
                    return mapper.toResponse(weeklyScheduleRepository.save(schedule));
                })
                .toList();
    }

    @Override
    @Transactional
    public WeeklyScheduleResponse updateWeeklySchedule(UUID carWashId, UUID scheduleId, WeeklyScheduleRequest request) {
        WeeklySchedule schedule = weeklyScheduleRepository.findByIdAndProfile_CarWashRegistration_Id(scheduleId, carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Weekly schedule not found"));

        mapper.updateWeeklyScheduleFromRequest(request, schedule);
        return mapper.toResponse(weeklyScheduleRepository.save(schedule));
    }

    @Override
    @Transactional
    public void toggleWeeklyScheduleAvailability(UUID carWashId, UUID scheduleId) {
        WeeklySchedule schedule = weeklyScheduleRepository.findByIdAndProfile_CarWashRegistration_Id(scheduleId, carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Weekly schedule not found"));
        schedule.setAvailable(!schedule.isAvailable());
        weeklyScheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public List<WeeklyScheduleResponse> setAllWeeklySchedulesWithSameInterval(
            UUID carWashId, LocalTime startTime, LocalTime endTime,
            int intervalMinutes, boolean available) {

        validateTimeInterval(startTime, endTime, intervalMinutes);
        CarWashProfile profile = getProfile(carWashId);

        // Clear existing schedules
        weeklyScheduleRepository.deleteByProfile_CarWashRegistration_Id(carWashId);

        // Create new schedules for all days of week
        return Arrays.stream(DayOfWeek.values())
                .map(day -> createWeeklySchedule(profile, day, startTime, endTime, intervalMinutes, available))
                .map(mapper::toResponse)
                .toList();
    }

    private WeeklySchedule createWeeklySchedule(CarWashProfile profile, DayOfWeek day,
                                                LocalTime startTime, LocalTime endTime,
                                                int intervalMinutes, boolean available) {
        WeeklySchedule schedule = new WeeklySchedule();
        schedule.setProfile(profile);
        schedule.setDayOfWeek(day);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setAppointmentIntervalMinutes(intervalMinutes);
        schedule.setAvailable(available);
        return weeklyScheduleRepository.save(schedule);
    }

    // Special days implementation
    @Override
    public List<SpecialDayResponse> getAllSpecialDays(UUID carWashId, boolean activeOnly) {
        List<SpecialDay> specialDays = activeOnly ?
                specialDayRepository.findByProfile_CarWashRegistration_IdAndActive(carWashId, true) :
                specialDayRepository.findByProfile_CarWashRegistration_Id(carWashId);

        return specialDays.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<SpecialDayResponse> getSpecialDaysByDateRange(
            UUID carWashId, LocalDate startDate, LocalDate endDate, boolean activeOnly) {

        List<SpecialDay> specialDays = activeOnly ?
                specialDayRepository.findByProfile_CarWashRegistration_IdAndDateBetweenAndActive(
                        carWashId, startDate, endDate, true) :
                specialDayRepository.findByProfile_CarWashRegistration_IdAndDateBetween(
                        carWashId, startDate, endDate);

        return specialDays.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public SpecialDayResponse getSpecialDay(UUID carWashId, UUID specialDayId, boolean includeInactive) {
        SpecialDay specialDay = includeInactive ?
                specialDayRepository.findByIdAndProfile_CarWashRegistration_IdIncludingInactive(specialDayId, carWashId)
                        .orElseThrow(() -> new ResourceNotFoundException("Special day not found")) :
                specialDayRepository.findByIdAndProfile_CarWashRegistration_Id(specialDayId, carWashId)
                        .orElseThrow(() -> new ResourceNotFoundException("Special day not found"));

        return mapper.toResponse(specialDay);
    }

    @Override
    @Transactional
    public List<SpecialDayResponse> createSpecialDays(UUID carWashId, List<SpecialDayRequest> requests) {
        CarWashProfile profile = getProfile(carWashId);
        return requests.stream()
                .map(request -> {
                    SpecialDay specialDay = mapper.toEntity(request);
                    specialDay.setProfile(profile);
                    specialDay.setActive(true);
                    return mapper.toResponse(specialDayRepository.save(specialDay));
                })
                .toList();
    }

    @Override
    @Transactional
    public SpecialDayResponse updateSpecialDay(UUID carWashId, UUID specialDayId, SpecialDayRequest request) {
        SpecialDay specialDay = specialDayRepository.findByIdAndProfile_CarWashRegistration_Id(specialDayId, carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Special day not found"));

        mapper.updateSpecialDayFromRequest(request, specialDay);
        return mapper.toResponse(specialDayRepository.save(specialDay));
    }

    @Override
    @Transactional
    public void deleteSpecialDay(UUID carWashId, UUID specialDayId) {
        specialDayRepository.updateActiveStatus(specialDayId, carWashId, false);
    }

    @Override
    @Transactional
    public void restoreSpecialDay(UUID carWashId, UUID specialDayId) {
        specialDayRepository.updateActiveStatus(specialDayId, carWashId, true);
    }

    // Availability implementation
    @Override
    public List<TimeSlotResponse> getAvailableTimeSlots(UUID carWashId, LocalDate date) {

        Optional<SpecialDay> specialDay = specialDayRepository
                .findByProfile_CarWashRegistration_IdAndDateAndActive(carWashId, date, true);

        if (specialDay.isPresent()) {

            if (specialDay.get().getStartTime() == null || specialDay.get().getEndTime() == null) {
                return Collections.emptyList();
            }
            return generateTimeSlots(
                    date.atTime(specialDay.get().getStartTime()),
                    date.atTime(specialDay.get().getEndTime()),
                    getIntervalForDayOfWeek(carWashId, date.getDayOfWeek())
            );
        }

        // Otherwise check regular schedule
        return handleRegularDayAvailability(carWashId, date, date.getDayOfWeek());
    }

    private List<TimeSlotResponse> handleSpecialDayAvailability(LocalDate date, SpecialDay specialDay, DayOfWeek dayOfWeek) {
        if (specialDay.getStartTime() == null || specialDay.getEndTime() == null) {
            return Collections.emptyList();
        }

        int interval = getIntervalForDayOfWeek(specialDay.getId(), dayOfWeek);
        return generateTimeSlots(
                date.atTime(specialDay.getStartTime()),
                date.atTime(specialDay.getEndTime()),
                interval
        );
    }

    private List<TimeSlotResponse> handleRegularDayAvailability(UUID carWashId, LocalDate date, DayOfWeek dayOfWeek) {
        return weeklyScheduleRepository
                .findByProfile_CarWashRegistration_IdAndDayOfWeek(carWashId, dayOfWeek)
                .stream()
                .filter(WeeklySchedule::isAvailable)
                .findFirst()
                .map(schedule -> generateTimeSlots(
                        date.atTime(schedule.getStartTime()),
                        date.atTime(schedule.getEndTime()),
                        schedule.getAppointmentIntervalMinutes()
                ))
                .orElse(Collections.emptyList());
    }

    // Helper methods
    private List<TimeSlotResponse> generateTimeSlots(LocalDateTime start, LocalDateTime end, int intervalMinutes) {
        validateTimeInterval(start.toLocalTime(), end.toLocalTime(), intervalMinutes);

        List<TimeSlotResponse> slots = new ArrayList<>();
        LocalDateTime current = start;

        while (!current.plusMinutes(intervalMinutes).isAfter(end)) {
            slots.add(new TimeSlotResponse(
                    current,
                    current.plusMinutes(intervalMinutes),
                    "AVAILABLE"
            ));
            current = current.plusMinutes(intervalMinutes);
        }

        return slots;
    }

    private void validateTimeInterval(LocalTime startTime, LocalTime endTime, int intervalMinutes) {
        if (intervalMinutes <= 0) {
            throw new IllegalArgumentException("Interval must be positive");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end times cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }

    private int getIntervalForDayOfWeek(UUID carWashId, DayOfWeek dayOfWeek) {
        return weeklyScheduleRepository
                .findByProfile_CarWashRegistration_IdAndDayOfWeek(carWashId, dayOfWeek)
                .stream()
                .findFirst()
                .map(WeeklySchedule::getAppointmentIntervalMinutes)
                .orElseThrow(() -> new IllegalStateException("No schedule found for day " + dayOfWeek));
    }


    private CarWashProfile getProfile(UUID carWashId) {
        return profileRepository.findByCarWashRegistration_Id(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Car wash profile not found"));
    }
}