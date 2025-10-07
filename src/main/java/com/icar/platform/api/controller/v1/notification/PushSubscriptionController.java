package com.icar.platform.api.controller.v1.notification;

import com.icar.platform.api.dto.request.notification.PushSubscriptionRequest;
import com.icar.platform.application.service.notification.PushSubscriptionService;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/push")
@RequiredArgsConstructor
public class PushSubscriptionController {

    private final PushSubscriptionService pushSubscriptionService;
    private final CarWashRegistrationDataRepository carWashRepository;

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(
            @RequestBody PushSubscriptionRequest request,
            @AuthenticationPrincipal UserDetails loggedUser
    ) {
        String userEmail = loggedUser.getUsername();
        CarWashRegistration carWash = carWashRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado no banco de dados."));

        UUID profileId = carWash.getProfileId();

        pushSubscriptionService.subscribe(profileId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestBody PushSubscriptionRequest request) {
        pushSubscriptionService.unsubscribe(request.getEndpoint());
        return ResponseEntity.ok().build();
    }
}