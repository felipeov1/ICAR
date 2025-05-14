package com.icar.plataform.api.controller.v1.carwash.profile;

import com.icar.plataform.api.dto.request.carwash.profile.CarWashScheduleConfigRequest;
import com.icar.plataform.application.service.carwash.profile.CarWashProfileService;
import com.icar.plataform.application.service.carwash.profile.ScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Car Wash Profile - Schedule", description = "Manage scheduling and available time slots")
@RestController
@RequestMapping("/api/v1/carwashes/{carwashId}/profile/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Void> setScheduleConfiguration(
            @PathVariable UUID carwashId,
            @Valid @RequestBody CarWashScheduleConfigRequest request) {
        scheduleService.setScheduleConfiguration(carwashId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalDateTime>> getAvailableSlots(
            @PathVariable UUID carwashId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.getAvailableSlots(carwashId, date));
    }
}
