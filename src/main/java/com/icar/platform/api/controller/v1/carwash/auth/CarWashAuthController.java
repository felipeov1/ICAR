package com.icar.platform.api.controller.v1.carwash.auth;

import com.icar.platform.api.dto.request.auth.LoginRequest;
import com.icar.platform.api.dto.request.auth.RefreshTokenRequest;
import com.icar.platform.api.dto.request.carwash.auth.ChangePasswordRequest;
import com.icar.platform.api.dto.response.auth.LoginResponse;
// MUDANÇA AQUI: importe a INTERFACE, não a implementação
import com.icar.platform.application.service.carwash.auth.CarWashAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CarWash Auth", description = "Autenticação para donos de lava-rápidos")
@RestController
@RequestMapping("/api/v1/carwash/auth")
@RequiredArgsConstructor
public class CarWashAuthController {

    private final CarWashAuthService carWashAuthService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        LoginResponse loginResponse = carWashAuthService.authenticate(loginRequest, request, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        LoginResponse response = carWashAuthService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        carWashAuthService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('CARWASH')")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        carWashAuthService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}