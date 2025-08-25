// Caminho: com/icar/platform/api/controller/v1/auth/AuthController.java
package com.icar.platform.api.controller.v1.auth;

import com.icar.platform.api.dto.request.auth.LoginRequest;
import com.icar.platform.api.dto.request.auth.RefreshTokenRequest;
import com.icar.platform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.platform.api.dto.response.auth.EmailVerificationResponse;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import com.icar.platform.api.dto.response.auth.RegisterCustomerResponse;
import com.icar.platform.api.dto.response.customer.AccessTokenResponse;
import com.icar.platform.application.service.auth.AuthService;
import com.icar.platform.application.service.auth.PasswordResetService;
import com.icar.platform.application.service.customer.CustomerAuthService;
import com.icar.platform.infrastructure.validation.exception.CustomValidationException;
import com.icar.platform.shared.exception.BusinessException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Tag(name = "Customer Auth", description = "Security to access and register")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CustomerAuthService customerAuthService;
    private final PasswordResetService passwordResetService;

    private static final String REFRESH_TOKEN_COOKIE = "refresh_token"; // Nome unificado

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        LoginResponse loginResponse = authService.authenticate(loginRequest, request, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        if (request.refreshToken() == null) {
            throw new BusinessException("Refresh token não encontrado no corpo da requisição.");
        }

        LoginResponse response = authService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterCustomerRequest request) {
        try {
            RegisterCustomerResponse response = customerAuthService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CustomValidationException e) {
            return ResponseEntity.badRequest().body(e.getErrors());
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage(),
                    "error", "business_error"
            ));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        HttpHeaders headers = new HttpHeaders();
        try {
            EmailVerificationResponse response = customerAuthService.verifyEmail(token);
            String encodedEmail = URLEncoder.encode(response.getEmail(), StandardCharsets.UTF_8);
            headers.setLocation(URI.create(frontendUrl + "/entrar?verified=true&email=" + encodedEmail));
        } catch (BusinessException e) {
            String errorCode = e.getMessage().toLowerCase().contains("expired") ? "token_expired" : "invalid_token";
            headers.setLocation(URI.create(frontendUrl + "/entrar?error=" + errorCode));
        }
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<?> resendConfirmationEmail(@RequestBody Map<String, String> request) {
        // CORRIGIDO: A chamada agora é válida
        customerAuthService.resendConfirmationEmail(request.get("email"));
        return ResponseEntity.ok(Map.of("message", "Confirmation email resent successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String userType = request.get("userType");
        if (email == null || userType == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "E-mail e tipo de usuário são obrigatórios."));
        }
        passwordResetService.createPasswordResetRequest(email, userType);
        return ResponseEntity.ok(Map.of("message", "Se uma conta com este e-mail existir, um link de redefinição foi enviado."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        if (token == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Token e nova senha são obrigatórios."));
        }
        passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso."));
    }
}