package com.icar.platform.api.controller.v1.auth;

import com.icar.platform.api.dto.request.auth.LoginRequest;
import com.icar.platform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.platform.api.dto.response.auth.EmailVerificationResponse;
import com.icar.platform.api.dto.response.customer.AccessTokenResponse;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import com.icar.platform.api.dto.response.auth.RegisterCustomerResponse;
import com.icar.platform.application.service.auth.AuthService;
import com.icar.platform.application.service.customer.CustomerAuthService;
import com.icar.platform.application.service.email.EmailService;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.email.EmailVerification;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.domain.repository.email.EmailVerificationRepository;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import com.icar.platform.infrastructure.validation.exception.CustomValidationException;
import com.icar.platform.shared.exception.BusinessException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CookieValue;


import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Customer Auth", description = "Security to access and register")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CustomerAuthService customerAuthService;
    private final TokenGenerator tokenGenerator;
    private final CustomerRepository customerRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${jwt.expiration.access-token}")
    private long accessTokenExpirationMs;


    private static final int THIRTY_DAYS_IN_SECONDS = 2592000;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
                                               HttpServletResponse response,
                                               @RequestParam(required = false, defaultValue = "false") boolean rememberMe) {
        LoginResponse loginResponse = authService.authenticate(request, rememberMe);

        int maxAge = rememberMe ? THIRTY_DAYS_IN_SECONDS : -1;

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", loginResponse.refreshToken())
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(
            @CookieValue(name = "refresh_token") String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessException("Refresh token not found in cookie");
        }

        if (!tokenGenerator.validateToken(refreshToken, null)) {
            throw new BusinessException("Invalid or expired refresh token");
        }

        String email = tokenGenerator.getEmailFromToken(refreshToken);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found for the given token"));

        String newAccessToken = tokenGenerator.generateAccessToken(customer);
        long accessTokenValiditySeconds = accessTokenExpirationMs / 1000;

        return ResponseEntity.ok(
                new AccessTokenResponse(newAccessToken, (int) accessTokenValiditySeconds)
        );


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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "Internal server error",
                    "error", "internal_error"
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
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Email not found"));

        if (customer.isEmailVerified()) {
            throw new BusinessException("Email already verified");
        }

        // CORRECTION: Using the newly added 'findByCustomer' method.
        emailVerificationRepository.findByCustomer(customer).ifPresent(emailVerificationRepository::delete);

        EmailVerification verification = EmailVerification.builder()
                .customer(customer)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        emailVerificationRepository.save(verification);
        emailService.sendVerificationEmail(customer.getEmail(), verification.getToken());

        return ResponseEntity.ok(Map.of("message", "Confirmation email resent successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }
}
