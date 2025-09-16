package com.icar.platform.api.controller.v1.customer; // O package pode ser 'customer' ou um novo 'notification'

import com.icar.platform.application.service.customer.NotificationSubscriptionServiceImpl;
import com.icar.platform.infrastructure.security.service.UserDetailsImpl; // 1. Importe sua classe UserDetailsImpl
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notifications", description = "Endpoints for user notifications and subscriptions")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationSubscriptionController {

    private final NotificationSubscriptionServiceImpl subscriptionService;

    @Operation(summary = "Subscribe to dry wash launch notification", description = "Registers the authenticated customer's interest in the upcoming dry wash feature.")
    @PostMapping("/subscribe-dry-wash")
    public ResponseEntity<Void> subscribeToDryWash(
            @AuthenticationPrincipal UserDetailsImpl authenticatedUser
    ) {
        subscriptionService.subscribeToDryWash(authenticatedUser.getId());
        return ResponseEntity.ok().build();
    }
}