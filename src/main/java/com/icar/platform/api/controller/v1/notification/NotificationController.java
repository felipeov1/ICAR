package com.icar.platform.api.controller.v1.notification;

import com.icar.platform.api.dto.response.notification.NotificationResponse;
import com.icar.platform.application.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Car Wash - Notifications", description = "Endpoints to manage car wash notifications")
@RestController
@RequestMapping("/api/v1/profile/{profileId}/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getNotifications(
            @PathVariable UUID profileId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(notificationService.getNotificationsForProfile(profileId, page, size));
    }

    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable UUID profileId) {
        notificationService.markAllAsRead(profileId);
        return ResponseEntity.ok().build();
    }
}