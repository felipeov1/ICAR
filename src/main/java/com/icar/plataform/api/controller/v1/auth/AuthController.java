package com.icar.plataform.api.controller.v1.auth;

import com.icar.plataform.api.dto.request.auth.LoginRequest;
import com.icar.plataform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.plataform.api.dto.response.auth.EmailVerificationResponse;
import com.icar.plataform.api.dto.response.customer.AccessTokenResponse;
import com.icar.plataform.api.dto.response.auth.LoginResponse;
import com.icar.plataform.api.dto.response.auth.RegisterCustomerResponse;
import com.icar.plataform.application.service.auth.AuthService;
import com.icar.plataform.application.service.customer.CustomerAuthService;
import com.icar.plataform.application.service.email.EmailService;
import com.icar.plataform.domain.model.customer.Customer;
import com.icar.plataform.domain.model.email.EmailVerification;
import com.icar.plataform.domain.repository.customer.CustomerRepository;
import com.icar.plataform.domain.repository.email.EmailVerificationRepository;
import com.icar.plataform.infrastructure.security.utils.TokenGenerator;
import com.icar.plataform.infrastructure.validation.exception.CustomValidationException;
import com.icar.plataform.shared.exception.BusinessException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    // ✅ Injeta a URL do frontend a partir do application.properties
    @Value("${app.frontend-url}")
    private String frontendUrl;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
                                               HttpServletResponse response,
                                               @RequestParam(required = false) boolean rememberMe) {
        LoginResponse loginResponse = authService.authenticate(request);

        int maxAge = rememberMe ? 2592000 : 86400; // 30 dias vs 1 dia

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", loginResponse.refreshToken())
                .httpOnly(true)
                .secure(true) // Lembre-se que em produção isso exige HTTPS
                .path("/api/v1/auth/refresh")
                .maxAge(maxAge)
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
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        String newAccessToken = tokenGenerator.generateAccessToken(customer);
        return ResponseEntity.ok(new AccessTokenResponse(newAccessToken, 3600));
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
                    "message", "Erro interno no servidor",
                    "error", "internal_error"
            ));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        HttpHeaders headers = new HttpHeaders();
        try {
            // Tenta verificar o e-mail
            customerAuthService.verifyEmail(token);
            // Se der certo, redireciona para o login no frontend com uma mensagem de sucesso
            headers.setLocation(URI.create(frontendUrl + "/login?verified=true"));
        } catch (BusinessException e) {
            // Se o token for inválido/expirado, redireciona para uma página de erro ou login com mensagem de erro
            String errorCode = e.getMessage().toLowerCase().contains("expired") ? "token_expired" : "invalid_token";
            headers.setLocation(URI.create(frontendUrl + "/login?error=" + errorCode));
        }
        // Retorna uma resposta 302 (Found), que o navegador entende como um redirecionamento
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


    @PostMapping("/resend-confirmation")
    public ResponseEntity<?> resendConfirmationEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("E-mail não encontrado"));

        if (customer.isEmailVerified()) {
            throw new BusinessException("E-mail já verificado");
        }

        emailVerificationRepository.deleteByCustomer(customer);
        EmailVerification verification = EmailVerification.builder()
                .customer(customer)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        emailVerificationRepository.save(verification);
        emailService.sendVerificationEmail(customer.getEmail(), verification.getToken());

        return ResponseEntity.ok(Map.of("message", "E-mail de confirmação reenviado com sucesso"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .maxAge(0)
                .path("/api/v1/auth/refresh") // Garanta que o path é o mesmo do cookie original
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }
}
