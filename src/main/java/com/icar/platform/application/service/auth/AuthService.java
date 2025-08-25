package com.icar.platform.application.service.auth;

import com.icar.platform.api.dto.request.auth.LoginRequest;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import com.icar.platform.api.dto.response.auth.UserDto;
import com.icar.platform.api.dto.response.customer.AccessTokenResponse;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.domain.repository.customer.CustomerRepository;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import com.icar.platform.shared.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final CarWashRegistrationDataRepository carWashRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    @Value("${jwt.expiration.access-token}")
    private long accessTokenExpirationMs;

    private static final String CUSTOMER_REFRESH_TOKEN_COOKIE = "refresh_token";

    public LoginResponse authenticate(LoginRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Customer customer = customerRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), customer.getPassword())) {
            throw new BusinessException("Credenciais inválidas");
        }

        if (!customer.isEmailVerified()) {
            throw new BusinessException("E-mail não verificado");
        }

        String accessToken = tokenGenerator.generateAccessToken(customer);
        String refreshToken = tokenGenerator.generateRefreshToken(customer);

        UserDto userDto = new UserDto();
        userDto.setId(customer.getId());
        userDto.setId(customer.getId());
        userDto.setId(customer.getId());
        userDto.setEmail(customer.getEmail());
        userDto.setFullName(customer.getFullName());
        userDto.setRole("CUSTOMER");
        userDto.setEmailVerified(customer.isEmailVerified());

        return new LoginResponse(accessToken, refreshToken, userDto);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (refreshToken == null || !tokenGenerator.validateToken(refreshToken, null)) {
            throw new BusinessException("Refresh token inválido ou expirado");
        }

        String email = tokenGenerator.getSubjectFromToken(refreshToken);

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado: " + email));

        String newAccessToken = tokenGenerator.generateAccessToken(customer);
        String newRefreshToken = tokenGenerator.generateRefreshToken(customer);

        UserDto userDto = new UserDto();
        userDto.setId(customer.getId());
        userDto.setEmail(customer.getEmail());
        userDto.setFullName(customer.getFullName());
        userDto.setRole("CUSTOMER");
        userDto.setEmailVerified(customer.isEmailVerified());

        return new LoginResponse(newAccessToken, newRefreshToken, userDto);
    }
}