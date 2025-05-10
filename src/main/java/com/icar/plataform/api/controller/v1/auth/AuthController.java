package com.icar.plataform.api.controller.v1.auth;

import com.icar.plataform.api.dto.request.auth.LoginRequest;
import com.icar.plataform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.plataform.api.dto.response.auth.EmailVerificationResponse;
import com.icar.plataform.api.dto.response.customer.AccessTokenResponse;
import com.icar.plataform.api.dto.response.auth.LoginResponse;
import com.icar.plataform.api.dto.response.auth.RegisterCustomerResponse;
import com.icar.plataform.application.service.auth.AuthService;
import com.icar.plataform.application.service.customer.CustomerService;
import com.icar.plataform.domain.model.customer.Customer;
import com.icar.plataform.domain.repository.customer.CustomerRepository;
import com.icar.plataform.infrastructure.security.utils.TokenGenerator;
import com.icar.plataform.infrastructure.validation.exception.CustomValidationException;
import com.icar.plataform.shared.exception.BusinessException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CustomerService customerService;
    private final TokenGenerator tokenGenerator; // Adicionado
    private final CustomerRepository customerRepository; // Adicionado

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
                                               HttpServletResponse response) {
        LoginResponse loginResponse = authService.authenticate(request);

        // Cookie apenas para Refresh Token (HTTP-only e Secure)
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", loginResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(2592000) // 30 dias em segundos
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(
            @CookieValue("refresh_token") String refreshToken) {

        if (!tokenGenerator.validateToken(refreshToken, null)) {
            throw new BusinessException("Refresh token inválido");
        }

        String email = tokenGenerator.getEmailFromToken(refreshToken);
        Customer customer = customerRepository.findByEmail(email) // Corrigido
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        String newAccessToken = tokenGenerator.generateAccessToken(customer);

        return ResponseEntity.ok(new AccessTokenResponse(newAccessToken, 3600));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterCustomerRequest request) {
        try {
            RegisterCustomerResponse response = customerService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CustomValidationException e) {
            return ResponseEntity.badRequest().body(e.getErrors());
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<EmailVerificationResponse> verifyEmail(
            @RequestParam String token) {
        EmailVerificationResponse response = customerService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Invalida o cookie do refresh token
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    private void addAuthCookies(HttpServletResponse response, LoginRequest request,
                                LoginResponse loginResponse) {
        // Auth token cookie
        ResponseCookie tokenCookie = ResponseCookie.from("auth_token", loginResponse.token())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(loginResponse.expiresIn())
                .sameSite("Strict")
                .build();

        // User data cookie (simplified)
        ResponseCookie userCookie = ResponseCookie.from("user_email", request.email())
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(loginResponse.expiresIn())
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, userCookie.toString());
    }
}