package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.SpecialDayRequest;
import com.icar.platform.api.dto.request.carwash.profile.WeeklyScheduleRequest;
import com.icar.platform.api.dto.response.carwash.profile.SpecialDayResponse;
import com.icar.platform.api.dto.response.carwash.profile.TimeSlotResponse;
import com.icar.platform.api.dto.response.carwash.profile.WeeklyScheduleResponse;
import com.icar.platform.api.mapper.carwash.ScheduleMapper;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.carwash.profile.AppointmentConfig;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.carwash.profile.SpecialDay;
import com.icar.platform.domain.model.carwash.profile.WeeklySchedule;
import com.icar.platform.domain.repository.appointment.CarWashAppointmentRepository;
import com.icar.platform.domain.repository.carwash.profile.AppointmentConfigRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.domain.repository.carwash.profile.SpecialDayRepository;
import com.icar.platform.domain.repository.carwash.profile.WeeklyScheduleRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleManagementServiceImpl implements ScheduleManagementService {

    private final CarWashProfileRepository profileRepository;
    private final WeeklyScheduleRepository weeklyScheduleRepository;
    private final SpecialDayRepository specialDayRepository;
    private final AppointmentConfigRepository appointmentConfigRepository;
    private final ScheduleMapper mapper;
    private final CarWashAppointmentRepository appointmentRepository;

    @Override
    public List<TimeSlotResponse> getAvailableTimeSlots(UUID profileId, LocalDate date, Integer serviceDurationMinutes) {
        AppointmentConfig config = appointmentConfigRepository.findByProfile_Id(profileId)
                .orElseThrow(() -> new BusinessException("Configuração de agendamento não encontrada."));


        final ZoneId profileZoneId = ZoneId.of("America/Sao_Paulo");

        LocalDate todayInProfileZone = LocalDate.now(profileZoneId);
        LocalDate maxDateAllowed = todayInProfileZone.plusDays(config.getMaxAdvanceBookingDays());
        if (date.isAfter(maxDateAllowed) || date.isBefore(todayInProfileZone)) {
            return Collections.emptyList();
        }

        final int GAP_MINUTES = config.getGapMinutes();
        final boolean ALLOW_OVERTIME = config.isAllowOvertime();

        if (GAP_MINUTES < 0) {
            throw new BusinessException("O intervalo (GAP) entre agendamentos não pode ser negativo.");
        }

        Optional<SpecialDay> specialDayOpt = specialDayRepository.findByProfile_IdAndDateAndActive(profileId, date, true);
        LocalTime dayStartTime, dayEndTime;

        if (specialDayOpt.isPresent() && specialDayOpt.get().getStartTime() != null) {
            dayStartTime = specialDayOpt.get().getStartTime();
            dayEndTime = specialDayOpt.get().getEndTime();
        } else {
            Optional<WeeklySchedule> weeklyOpt = weeklyScheduleRepository
                    .findByProfile_IdAndDayOfWeek(profileId, date.getDayOfWeek())
                    .stream()
                    .filter(WeeklySchedule::isAvailable)
                    .findFirst();

            if (weeklyOpt.isPresent()) {
                dayStartTime = weeklyOpt.get().getStartTime();
                dayEndTime = weeklyOpt.get().getEndTime();
            } else {
                return Collections.emptyList();
            }
        }

        ZonedDateTime queryStart = date.atStartOfDay(profileZoneId);
        ZonedDateTime queryEnd = queryStart.plusDays(1);
        List<CarWashAppointment> bookedAppointments = appointmentRepository.findBookedSlotsByProfileIdAndDateRange(profileId, queryStart, queryEnd);
        bookedAppointments.sort(Comparator.comparing(CarWashAppointment::getDateTime));

        List<TimeSlotResponse> availableSlots = new ArrayList<>();
        ZonedDateTime earliestBookingTime = ZonedDateTime.now(profileZoneId).plusMinutes(config.getMinAdvanceNoticeMinutes());

        LocalDateTime potentialStartTime = date.atTime(dayStartTime);

        while (!potentialStartTime.toLocalTime().isAfter(dayEndTime)) {
            ZonedDateTime potentialZonedDateTime = potentialStartTime.atZone(profileZoneId);

            if (potentialZonedDateTime.isBefore(earliestBookingTime)) {
                potentialStartTime = potentialStartTime.plusMinutes(GAP_MINUTES > 0 ? GAP_MINUTES : 15);
                continue;
            }

            LocalDateTime serviceEndTime = potentialStartTime.plusMinutes(serviceDurationMinutes);

            if (!ALLOW_OVERTIME && serviceEndTime.toLocalTime().isAfter(dayEndTime) && serviceEndTime.toLocalDate().isEqual(date)) {
                break;
            }

            boolean hasConflict = false;
            for (CarWashAppointment booked : bookedAppointments) {
                LocalDateTime bookedStartTime = booked.getDateTime().withZoneSameInstant(profileZoneId).toLocalDateTime();
                LocalDateTime bookedEndTimeWithGap = bookedStartTime.plusMinutes(booked.getTotalDurationMinutes() + GAP_MINUTES);

                if (potentialStartTime.isBefore(bookedEndTimeWithGap) && serviceEndTime.isAfter(bookedStartTime)) {
                    hasConflict = true;
                    potentialStartTime = bookedEndTimeWithGap;
                    break;
                }
            }

            if (!hasConflict) {
                availableSlots.add(new TimeSlotResponse(potentialZonedDateTime, serviceEndTime.atZone(profileZoneId), "AVAILABLE"));
                potentialStartTime = potentialStartTime.plusMinutes(GAP_MINUTES > 0 ? GAP_MINUTES : serviceDurationMinutes);
            }
        }

        return availableSlots;
    }

    @Override
    public List<String> getAvailableDates(UUID profileId, LocalDate startDate, LocalDate endDate) {
        AppointmentConfig config = appointmentConfigRepository.findByProfile_Id(profileId)
                .orElseThrow(() -> new BusinessException("Configuração de agendamento não encontrada."));

        LocalDate today = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        LocalDate effectiveStartDate = startDate.isBefore(today) ? today : startDate;

        LocalDate maxDateAllowed = today.plusDays(config.getMaxAdvanceBookingDays());
        LocalDate effectiveEndDate = endDate.isAfter(maxDateAllowed) ? maxDateAllowed : endDate;

        final int DUMMY_SERVICE_DURATION_FOR_CHECK = 15;

        return effectiveStartDate.datesUntil(effectiveEndDate.plusDays(1))
                .parallel()
                .filter(date -> !getAvailableTimeSlots(profileId, date, DUMMY_SERVICE_DURATION_FOR_CHECK).isEmpty())
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<WeeklyScheduleResponse> getAllWeeklySchedules(UUID profileId) {
        List<WeeklySchedule> schedules = weeklyScheduleRepository.findByProfile_Id(profileId);
        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma agenda semanal encontrada para o perfil: " + profileId);
        }
        return schedules.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public WeeklyScheduleResponse getWeeklySchedule(UUID profileId, UUID scheduleId) {
        return weeklyScheduleRepository.findByIdAndProfile_Id(scheduleId, profileId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda semanal não encontrada."));
    }

    @Override
    @Transactional
    public List<WeeklyScheduleResponse> createWeeklySchedules(UUID profileId, List<WeeklyScheduleRequest> requests) {
        CarWashProfile profile = getProfile(profileId);
        return requests.stream().map(request -> {
            WeeklySchedule schedule = mapper.toEntity(request);
            schedule.setProfile(profile);
            return mapper.toResponse(weeklyScheduleRepository.save(schedule));
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WeeklyScheduleResponse updateWeeklySchedule(UUID profileId, UUID scheduleId, WeeklyScheduleRequest request) {
        WeeklySchedule schedule = weeklyScheduleRepository.findByIdAndProfile_Id(scheduleId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda semanal não encontrada."));
        mapper.updateWeeklyScheduleFromRequest(request, schedule);
        return mapper.toResponse(weeklyScheduleRepository.save(schedule));
    }

    @Override
    @Transactional
    public void toggleWeeklyScheduleAvailability(UUID profileId, UUID scheduleId) {
        WeeklySchedule schedule = weeklyScheduleRepository.findByIdAndProfile_Id(scheduleId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda semanal não encontrada."));
        schedule.setAvailable(!schedule.isAvailable());
        weeklyScheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public List<WeeklyScheduleResponse> setAllWeeklySchedulesWithSameInterval(
            UUID profileId, LocalTime startTime, LocalTime endTime, int intervalMinutes, boolean available) {
        validateTimeInterval(startTime, endTime, intervalMinutes);
        CarWashProfile profile = getProfile(profileId);
        weeklyScheduleRepository.deleteByProfile_Id(profileId);
        return Arrays.stream(DayOfWeek.values())
                .map(day -> createWeeklySchedule(profile, day, startTime, endTime, intervalMinutes, available))
                .map(mapper::toResponse)
                .collect(Collectors.toList());
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

    @Override
    public List<SpecialDayResponse> getAllSpecialDays(UUID profileId, boolean activeOnly) {
        List<SpecialDay> specialDays = activeOnly ?
                specialDayRepository.findByProfile_IdAndActive(profileId, true) :
                specialDayRepository.findByProfile_Id(profileId);
        return specialDays.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<SpecialDayResponse> getSpecialDaysByDateRange(
            UUID profileId, LocalDate startDate, LocalDate endDate, boolean activeOnly) {
        List<SpecialDay> specialDays = activeOnly ?
                specialDayRepository.findByProfile_IdAndDateBetweenAndActive(profileId, startDate, endDate, true) :
                specialDayRepository.findByProfile_IdAndDateBetween(profileId, startDate, endDate);
        return specialDays.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public SpecialDayResponse getSpecialDay(UUID carWashId, UUID specialDayId, boolean includeInactive) {
        SpecialDay specialDay = includeInactive ?
                specialDayRepository.findByIdAndProfile_CarWashRegistration_IdIncludingInactive(specialDayId, carWashId)
                        .orElseThrow(() -> new ResourceNotFoundException("Dia especial não encontrado.")) :
                specialDayRepository.findByIdAndProfile_CarWashRegistration_Id(specialDayId, carWashId)
                        .orElseThrow(() -> new ResourceNotFoundException("Dia especial não encontrado."));
        return mapper.toResponse(specialDay);
    }

    @Override
    @Transactional
    public List<SpecialDayResponse> createSpecialDays(UUID profileId, List<SpecialDayRequest> requests) {
        CarWashProfile profile = getProfile(profileId);
        return requests.stream().map(request -> {
            SpecialDay specialDay = mapper.toEntity(request);
            specialDay.setProfile(profile);
            specialDay.setActive(true);
            return mapper.toResponse(specialDayRepository.save(specialDay));
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SpecialDayResponse updateSpecialDay(UUID carWashId, UUID specialDayId, SpecialDayRequest request) {
        SpecialDay specialDay = specialDayRepository.findByIdAndProfile_CarWashRegistration_Id(specialDayId, carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("Dia especial não encontrado."));
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

    private void validateTimeInterval(LocalTime startTime, LocalTime endTime, int intervalMinutes) {
        if (intervalMinutes <= 0) {
            throw new IllegalArgumentException("O intervalo deve ser positivo.");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("As horas de início e fim não podem ser nulas.");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("A hora de início deve ser anterior à hora de fim.");
        }
    }

    private CarWashProfile getProfile(UUID profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil do lava-rápido não encontrado."));
    }
}