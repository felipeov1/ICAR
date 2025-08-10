package com.icar.platform.api.controller.v1.carwash.auth;

import com.icar.platform.api.dto.request.carwash.CarWashLoginRequest;
import com.icar.platform.api.dto.request.carwash.auth.ChangePasswordRequest;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import com.icar.platform.api.dto.response.customer.AccessTokenResponse;
import com.icar.platform.application.service.carwash.auth.CarWashAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carwash/auth")
@RequiredArgsConstructor
public class CarWashAuthController {

    private final CarWashAuthService authService;
    private static final int THIRTY_DAYS_IN_SECONDS = 2592000;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody CarWashLoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(loginRequest);

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token_carwash", loginResponse.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(THIRTY_DAYS_IN_SECONDS)
                .sameSite("Lax")
                // .secure(true)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(@CookieValue(name = "refresh_token_carwash", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token_carwash", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change car wash password")
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}