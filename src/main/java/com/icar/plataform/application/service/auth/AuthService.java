package com.icar.plataform.application.service.auth;

import com.icar.plataform.api.dto.request.LoginRequest;
import com.icar.plataform.api.dto.response.CustomerResponse;
import com.icar.plataform.api.dto.response.LoginResponse;
import com.icar.plataform.domain.enums.UserStatus;
import com.icar.plataform.domain.model.Customer;
import com.icar.plataform.domain.repository.CustomerRepository;
import com.icar.plataform.infrastructure.security.utils.TokenGenerator;
import com.icar.plataform.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public LoginResponse authenticate(LoginRequest request) {
        Customer customer = customerRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), customer.getPassword())) {
            throw new BusinessException("Credenciais inválidas");
        }

        return new LoginResponse(
                tokenGenerator.generateAccessToken(customer), // Access Token (1 hora)
                tokenGenerator.generateRefreshToken(customer), // Refresh Token (30 dias)
                3600L, // expiresIn em segundos (1 hora)
                new CustomerResponse(
                        customer.getId(),          // UUID id
                        customer.getFullName(),   // String name
                        customer.getEmail(),      // String email
                        customer.isEmailVerified() // boolean emailVerified
                )
        );
    }
}