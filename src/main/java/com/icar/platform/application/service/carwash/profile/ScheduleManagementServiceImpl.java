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
import com.icar.platform.shared.exception.ConflictException;
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
    private final CarWashAppointmentRepository appointmentRepository;
    private final ScheduleMapper mapper;

    private void validateNoExistingAppointmentsInRange(UUID profileId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        List<CarWashAppointment> existingAppointments = appointmentRepository
                .findActiveByProfileIdAndDateTimeRange(profileId, startDateTime, endDateTime);

        if (!existingAppointments.isEmpty()) {
            int count = existingAppointments.size();
            String plural = count > 1 ? "s" : "";
            throw new ConflictException(
                    "Não é possível salvar a exceção, pois existe" + (count > 1 ? "m" : "") + " " +
                            count + " agendamento" + plural + " ativo" + plural + " no período selecionado. " +
                            "Por favor, cancele ou remarque o" + plural + " agendamento" + plural + " antes de continuar."
            );
        }
    }

    @Override
    public List<TimeSlotResponse> getAvailableTimeSlots(UUID profileId, LocalDate date, Integer serviceDurationMinutes, UUID appointmentIdToIgnore) {
        AppointmentConfig config = appointmentConfigRepository.findByProfile_Id(profileId)
                .orElseThrow(() -> new IllegalStateException("Configuração de agendamento não encontrada para o perfil: " + profileId));

        final ZoneId profileZoneId = ZoneId.of("America/Sao_Paulo");
        LocalDate todayInProfileZone = LocalDate.now(profileZoneId);
        LocalDate maxDateAllowed = todayInProfileZone.plusDays(config.getMaxAdvanceBookingDays());
        if (date.isAfter(maxDateAllowed) || date.isBefore(todayInProfileZone)) {
            return Collections.emptyList();
        }

        LocalTime dayStartTime, dayEndTime;
        int appointmentIntervalMinutes;

        Optional<WeeklySchedule> weeklyOpt = weeklyScheduleRepository.findByProfile_IdAndDayOfWeek(profileId, date.getDayOfWeek());
        Optional<SpecialDay> specialDayOpt = specialDayRepository.findActiveByProfileIdAndDate(profileId, date).stream().findFirst();

        if (specialDayOpt.isPresent()) {
            SpecialDay specialDay = specialDayOpt.get();
            if (specialDay.isClosed()) { return Collections.emptyList(); }
            dayStartTime = specialDay.getStartTime();
            dayEndTime = specialDay.getEndTime();
            appointmentIntervalMinutes = weeklyOpt
                    .map(WeeklySchedule::getAppointmentIntervalMinutes)
                    .orElseThrow(() -> new IllegalStateException("Configuração de intervalo não encontrada para " + date.getDayOfWeek()));
        } else {
            if (weeklyOpt.isPresent() && weeklyOpt.get().isAvailable()) {
                WeeklySchedule schedule = weeklyOpt.get();
                dayStartTime = schedule.getStartTime();
                dayEndTime = schedule.getEndTime();
                appointmentIntervalMinutes = schedule.getAppointmentIntervalMinutes();
            } else {
                return Collections.emptyList();
            }
        }
        return generateAndFilterTimeSlots(profileId, date, serviceDurationMinutes, config, dayStartTime, dayEndTime, profileZoneId, appointmentIntervalMinutes, appointmentIdToIgnore);
    }




    private List<TimeSlotResponse> generateAndFilterTimeSlots(UUID profileId, LocalDate date, int serviceDurationMinutes, AppointmentConfig config, LocalTime startTime, LocalTime endTime, ZoneId zoneId, int appointmentIntervalMinutes, UUID appointmentIdToIgnore) {
        final int GAP_MINUTES = config.getGapMinutes();

        LocalDateTime queryStart = date.atStartOfDay();
        LocalDateTime queryEnd = date.atTime(LocalTime.MAX);

        List<CarWashAppointment> bookedAppointments;
        if (appointmentIdToIgnore != null) {
            bookedAppointments = appointmentRepository.findBookedSlotsByProfileIdAndDateRangeExcludingId(profileId, queryStart, queryEnd, appointmentIdToIgnore);
        } else {
            bookedAppointments = appointmentRepository.findBookedSlotsByProfileIdAndDateRange(profileId, queryStart, queryEnd);
        }

        List<TimeSlotResponse> availableSlots = new ArrayList<>();
        LocalDateTime earliestBookingTime = LocalDateTime.now(zoneId).plusMinutes(config.getMinAdvanceNoticeMinutes());
        LocalDateTime cursorTime = date.atTime(startTime);
        LocalDateTime dayEndDateTime = date.atTime(endTime);

        int stepMinutes = appointmentIntervalMinutes > 0 ? appointmentIntervalMinutes : 15;

        while (!cursorTime.isAfter(dayEndDateTime)) {
            LocalDateTime serviceEndTime = cursorTime.plusMinutes(serviceDurationMinutes);

            if (!config.isAllowOvertime() && serviceEndTime.isAfter(dayEndDateTime)) {
                break;
            }

            if (cursorTime.isBefore(earliestBookingTime)) {
                cursorTime = cursorTime.plusMinutes(stepMinutes);
                continue;
            }

            boolean hasConflict = false;
            for (CarWashAppointment booked : bookedAppointments) {
                LocalDateTime bookedStart = booked.getDateTime();
                LocalDateTime bookedEnd = bookedStart.plusMinutes(booked.getTotalDurationMinutes());

                LocalDateTime bookedStartWithGap = bookedStart.minusMinutes(GAP_MINUTES);
                LocalDateTime bookedEndWithGap = bookedEnd.plusMinutes(GAP_MINUTES);

                if (cursorTime.isBefore(bookedEndWithGap) && serviceEndTime.isAfter(bookedStartWithGap)) {
                    hasConflict = true;
                    break;
                }
            }

            if (!hasConflict) {
                availableSlots.add(new TimeSlotResponse(cursorTime.atZone(zoneId), serviceEndTime.atZone(zoneId), "AVAILABLE"));
            }

            cursorTime = cursorTime.plusMinutes(stepMinutes);
        }
        return availableSlots;
    }

    @Override
    public List<String> getAvailableDates(UUID profileId, LocalDate startDate, LocalDate endDate) {
        AppointmentConfig config = appointmentConfigRepository.findByProfile_Id(profileId)
                .orElseThrow(() -> new IllegalStateException("Configuração de agendamento não encontrada para o perfil: " + profileId));

        LocalDate today = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        LocalDate effectiveStartDate = startDate.isBefore(today) ? today : startDate;

        LocalDate maxDateAllowed = today.plusDays(config.getMaxAdvanceBookingDays());
        LocalDate effectiveEndDate = endDate.isAfter(maxDateAllowed) ? maxDateAllowed : endDate;

        final int DUMMY_SERVICE_DURATION_FOR_CHECK = 15;

        return effectiveStartDate.datesUntil(effectiveEndDate.plusDays(1))
                .parallel()
                .filter(date -> !getAvailableTimeSlots(profileId, date, DUMMY_SERVICE_DURATION_FOR_CHECK, null).isEmpty())
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpecialDayResponse> getAllSpecialDays(UUID profileId) {
        LocalDate today = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        return specialDayRepository.findByProfile_IdAndEndDateGreaterThanEqual(profileId, today)
                .stream()
                .map(entity -> new SpecialDayResponse(
                        entity.getId(),
                        entity.getStartDate(),
                        entity.getEndDate(),
                        entity.getDescription(),
                        entity.isClosed(),
                        entity.getStartTime(),
                        entity.getEndTime()
                ))
                .sorted(Comparator.comparing(SpecialDayResponse::startDate))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public SpecialDayResponse createSpecialDay(UUID profileId, SpecialDayRequest request) {
        CarWashProfile profile = getProfile(profileId);

        validateNoOverlapping(profileId, request.getStartDate(), request.getEndDate(), null);
        validateNoExistingAppointmentsInRange(profileId, request.getStartDate(), request.getEndDate());

        SpecialDay specialDay = new SpecialDay();
        specialDay.setProfile(profile);
        specialDay.setStartDate(request.getStartDate());
        specialDay.setEndDate(request.getEndDate());
        specialDay.setDescription(request.getDescription());
        specialDay.setClosed(request.getIsClosed());

        if (request.getIsClosed()) {
            specialDay.setStartTime(null);
            specialDay.setEndTime(null);
        } else {
            specialDay.setStartTime(request.getStartTime());
            specialDay.setEndTime(request.getEndTime());
        }

        SpecialDay savedEntity = specialDayRepository.save(specialDay);
        return mapper.toResponse(savedEntity);
    }


    @Override
    @Transactional
    public SpecialDayResponse updateSpecialDay(UUID profileId, UUID specialDayId, SpecialDayRequest request) {
        SpecialDay specialDay = specialDayRepository.findByIdAndProfileIdIncludeDeleted(specialDayId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Dia especial não encontrado com ID: " + specialDayId));

        validateNoOverlapping(profileId, request.getStartDate(), request.getEndDate(), specialDayId);
        validateNoExistingAppointmentsInRange(profileId, request.getStartDate(), request.getEndDate());

        specialDay.setStartDate(request.getStartDate());
        specialDay.setEndDate(request.getEndDate());
        specialDay.setDescription(request.getDescription());
        specialDay.setClosed(request.getIsClosed());

        if (request.getIsClosed()) {
            specialDay.setStartTime(null);
            specialDay.setEndTime(null);
        } else {
            specialDay.setStartTime(request.getStartTime());
            specialDay.setEndTime(request.getEndTime());
        }

        SpecialDay savedEntity = specialDayRepository.save(specialDay);
        return mapper.toResponse(savedEntity);
    }


    @Override
    @Transactional
    public void deleteSpecialDay(UUID profileId, UUID specialDayId) {
        if (!specialDayRepository.existsByIdAndProfile_Id(specialDayId, profileId)) {
            throw new ResourceNotFoundException("Dia especial não encontrado com ID: " + specialDayId);
        }
        specialDayRepository.deleteById(specialDayId);
    }

    @Override
    public List<WeeklyScheduleResponse> getAllWeeklySchedules(UUID profileId) {
        return weeklyScheduleRepository.findByProfile_Id(profileId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
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
        List<WeeklySchedule> schedules = requests.stream().map(req -> {
            WeeklySchedule schedule = mapper.toEntity(req);
            schedule.setProfile(profile);
            return schedule;
        }).collect(Collectors.toList());
        return weeklyScheduleRepository.saveAll(schedules).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
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
    public List<WeeklyScheduleResponse> setAllWeeklySchedulesWithSameInterval(UUID profileId, LocalTime startTime, LocalTime endTime, int intervalMinutes, boolean available) {
        CarWashProfile profile = getProfile(profileId);
        weeklyScheduleRepository.deleteByProfile_Id(profileId);
        List<WeeklySchedule> newSchedules = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            WeeklySchedule schedule = new WeeklySchedule();
            schedule.setProfile(profile);
            schedule.setDayOfWeek(day);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setAppointmentIntervalMinutes(intervalMinutes);
            schedule.setAvailable(available);
            newSchedules.add(schedule);
        }
        return weeklyScheduleRepository.saveAll(newSchedules).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    private CarWashProfile getProfile(UUID profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com ID: " + profileId));
    }

    private void validateNoOverlapping(UUID profileId, LocalDate startDate, LocalDate endDate, UUID excludeId) {
        UUID idToExclude = (excludeId == null) ? UUID.fromString("00000000-0000-0000-0000-000000000000") : excludeId;
        List<SpecialDay> overlapping = specialDayRepository.findOverlappingRanges(profileId, startDate, endDate, idToExclude);
        if (!overlapping.isEmpty()) {
            throw new ConflictException("O período informado conflita com outra exceção já cadastrada.");
        }
    }


}