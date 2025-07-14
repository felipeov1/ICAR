package com.icar.platform.application.service.auth;

import com.icar.platform.api.dto.request.auth.LoginRequest;
import com.icar.platform.api.dto.response.customer.CustomerResponse;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import com.icar.platform.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    @Value("${jwt.expiration.access-token}")
    private long accessTokenExpirationMs;

    public LoginResponse authenticate(LoginRequest request, boolean rememberMe) {
        Customer customer = customerRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), customer.getPassword())) {
            throw new BusinessException("Credenciais inválidas");
        }

        if (!customer.isEmailVerified()) {
            throw new BusinessException("E-mail não verificado");
        }

        long refreshExpiration = rememberMe ? 2592000000L : 86400000L;
        String refreshToken = tokenGenerator.generateToken(customer, refreshExpiration);
        long accessTokenValiditySeconds = accessTokenExpirationMs / 1000;

        return new LoginResponse(
                tokenGenerator.generateAccessToken(customer),
                refreshToken,
                accessTokenValiditySeconds,
                new CustomerResponse(
                        customer.getId(),
                        customer.getFullName(),
                        customer.getEmail(),
                        customer.isEmailVerified()
                )
        );
    }
}
