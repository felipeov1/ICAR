package com.icar.platform.api.controller.v1.admin.notification;

import com.icar.platform.api.dto.request.notification.PushSubscriptionRequest;
import com.icar.platform.application.service.notification.PushSubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/push")
@RequiredArgsConstructor
@Tag(name = "Admin: Push Notifications")
public class AdminPushSubscriptionController {

    private final PushSubscriptionService pushSubscriptionService;

    @PostMapping("/subscribe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> subscribeAdmin(@RequestBody PushSubscriptionRequest request) {

        pushSubscriptionService.subscribeAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}