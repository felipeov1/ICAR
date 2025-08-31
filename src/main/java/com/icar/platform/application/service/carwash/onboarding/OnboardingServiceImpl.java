package com.icar.platform.application.service.carwash.onboarding;

import com.icar.platform.api.dto.request.carwash.onboarding.OnboardingRequest;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import com.icar.platform.api.dto.response.auth.UserDto;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import com.icar.platform.domain.model.carwash.offering.VehicleOfferingDetail;
import com.icar.platform.domain.model.carwash.profile.*;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.domain.repository.carwash.profile.*;
import com.icar.platform.application.service.carwash.profile.PhotoService;
import com.icar.platform.domain.enums.PhotoType;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboardingService {

    private final CarWashRegistrationDataRepository registrationRepository;
    private final CarWashProfileRepository profileRepository;
    private final AppointmentConfigRepository appointmentConfigRepository;
    private final WeeklyScheduleRepository weeklyScheduleRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final PhotoService photoService;
    private final TokenGenerator tokenGenerator;

    @Override
    @Transactional
    public LoginResponse processOnboarding(
            UUID carWashId,
            OnboardingRequest request,
            MultipartFile logo,
            MultipartFile coverPhoto,
            List<MultipartFile> galleryFiles,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        CarWashRegistration registration = registrationRepository.findById(carWashId)
                .orElseThrow(() -> new ResourceNotFoundException("CarWashRegistration not found with id: " + carWashId));

        if (profileRepository.findProfileIdByRegistrationId(carWashId).isPresent()) {
            throw new BusinessException("Onboarding já concluído para este lava-rápido.");
        }

        CarWashProfile profile = createAndSaveProfile(registration, request);

        photoService.addPhoto(profile.getId(), logo, PhotoType.LOGO);
        if (coverPhoto != null && !coverPhoto.isEmpty()) {
            photoService.addPhoto(profile.getId(), coverPhoto, PhotoType.COVER);
        }
        if (galleryFiles != null && !galleryFiles.isEmpty()) {
            photoService.addServicePhotos(profile.getId(), galleryFiles);
        }

        saveAppointmentConfig(profile, request.schedule().appointmentRules());
        saveWeeklySchedule(profile, request.schedule().week());
        saveVehicleTypes(profile, request.operation().vehicleTypes());
        saveOfferings(profile, request.services());

        profile.setOnboardingComplete(true);
        profileRepository.save(profile);

        String accessToken = tokenGenerator.generateAccessTokenForCarWash(registration);
        String refreshToken = tokenGenerator.generateRefreshTokenForCarWash(registration);

        UserDto userDto = new UserDto();
        userDto.setId(registration.getId());
        userDto.setProfileId(profile.getId());
        userDto.setEmail(registration.getEmail());
        userDto.setFullName(profile.getName());
        userDto.setRole("CARWASH");
        userDto.setEmailVerified(true);
        userDto.setIsProfileComplete(true);

        return new LoginResponse(accessToken, refreshToken, userDto);
    }

    private CarWashProfile createAndSaveProfile(CarWashRegistration registration, OnboardingRequest request) {
        CarWashProfile profile = new CarWashProfile();
        profile.setCarWashRegistration(registration);
        profile.setName(request.profile().name());
        profile.setSubdomain(registration.getTradeName().replaceAll("\\s+", "-").toLowerCase());
        profile.setWhatsapp(request.profile().whatsapp());
        profile.setDescription(request.profile().description());
        profile.setLocations(request.operation().locations().toArray(new String[0]));
        profile.setModalities(request.operation().modalities().toArray(new String[0]));
        profile.setObservations(request.operation().observations());
        profile.setRating(java.math.BigDecimal.ZERO);
        profile.setReviews(0);
        return profileRepository.save(profile);
    }

    private void saveAppointmentConfig(CarWashProfile profile, OnboardingRequest.AppointmentRulesPart rules) {
        AppointmentConfig config = new AppointmentConfig();
        config.setProfile(profile);
        config.setMinAdvanceNoticeMinutes(rules.minAdvanceNoticeMinutes());
        config.setMinEditNoticeMinutes(rules.minEditNoticeMinutes());
        config.setMinCancelNoticeMinutes(rules.minCancelNoticeMinutes());
        config.setAllowOvertime(rules.allowOvertime());
        config.setGapMinutes(rules.gapMinutes());
        config.setMaxAdvanceBookingDays(rules.maxAdvanceBookingDays());
        appointmentConfigRepository.save(config);
    }

    private DayOfWeek translateDayOfWeek(String portugueseDay) {
        return switch (portugueseDay.toLowerCase()) {
            case "segunda" -> DayOfWeek.MONDAY;
            case "terca" -> DayOfWeek.TUESDAY;
            case "quarta" -> DayOfWeek.WEDNESDAY;
            case "quinta" -> DayOfWeek.THURSDAY;
            case "sexta" -> DayOfWeek.FRIDAY;
            case "sabado" -> DayOfWeek.SATURDAY;
            case "domingo" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("Invalid day of week: " + portugueseDay);
        };
    }

    private void saveWeeklySchedule(CarWashProfile profile, java.util.Map<String, OnboardingRequest.DaySchedulePart> weekData) {
        List<WeeklySchedule> schedules = weekData.entrySet().stream().map(entry -> {
            WeeklySchedule schedule = new WeeklySchedule();
            schedule.setProfile(profile);
            schedule.setDayOfWeek(translateDayOfWeek(entry.getKey()));
            schedule.setAvailable(entry.getValue().available());
            schedule.setStartTime(LocalTime.parse(entry.getValue().startTime()));
            schedule.setEndTime(LocalTime.parse(entry.getValue().endTime()));
            schedule.setAppointmentIntervalMinutes(15);
            return schedule;
        }).collect(Collectors.toList());
        weeklyScheduleRepository.saveAll(schedules);
    }

    private void saveVehicleTypes(CarWashProfile profile, List<String> vehicleTypeNames) {
        List<VehicleType> vehicleTypes = vehicleTypeNames.stream().map(name -> {
            VehicleType vt = new VehicleType();
            vt.setProfileId(profile.getId());
            vt.setVehicleType(name);
            return vt;
        }).collect(Collectors.toList());
        vehicleTypeRepository.saveAll(vehicleTypes);
    }

    private void saveOfferings(CarWashProfile profile, List<OnboardingRequest.ServicePart> services) {
        List<CarWashOffering> offerings = new ArrayList<>();
        for (OnboardingRequest.ServicePart serviceDto : services) {
            CarWashOffering offering = new CarWashOffering();
            offering.setProfile(profile);
            offering.setName(serviceDto.name());
            offering.setDescription(serviceDto.description());
            offering.setServiceType(serviceDto.serviceType());

            Map<String, VehicleOfferingDetail> details = new HashMap<>();
            for (Map.Entry<String, OnboardingRequest.VehicleDetailPart> entry : serviceDto.vehicleDetails().entrySet()) {
                VehicleOfferingDetail detail = new VehicleOfferingDetail();
                detail.setPrice(entry.getValue().price());
                detail.setEstimatedTime(entry.getValue().durationMinutes());
                details.put(entry.getKey(), detail);
            }
            offering.setVehicleDetails(details);
            offerings.add(offering);
        }
        profile.setOfferings(offerings);
    }
}