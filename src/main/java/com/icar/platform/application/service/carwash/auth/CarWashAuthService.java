package com.icar.platform.application.service.carwash.auth;

import com.icar.platform.api.dto.request.carwash.CarWashLoginRequest;
import com.icar.platform.api.dto.request.carwash.auth.ChangePasswordRequest;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import com.icar.platform.api.dto.response.auth.UserDto;
import com.icar.platform.api.dto.response.customer.AccessTokenResponse;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import com.icar.platform.shared.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarWashAuthService {

    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final CarWashRegistrationDataRepository carWashRepository;
    private final CarWashProfileRepository profileRepository;

    @Value("${jwt.expiration.access-token}")
    private long accessTokenExpirationMs;

    public LoginResponse login(CarWashLoginRequest loginRequest) {
        CarWashRegistration carWash = carWashRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(loginRequest.password(), carWash.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        UUID profileId = carWash.getProfileId();
        boolean isProfileComplete = carWash.isProfileComplete();

        String accessToken = tokenGenerator.generateAccessTokenForCarWash(carWash);
        String refreshToken = tokenGenerator.generateRefreshTokenForCarWash(carWash);

        UserDto userDto = new UserDto();
        userDto.setId(carWash.getId());
        userDto.setProfileId(profileId);
        userDto.setEmail(carWash.getEmail());
        userDto.setFullName(carWash.getTradeName());
        userDto.setRole("CARWASH");
        userDto.setEmailVerified(true);
        userDto.setIsProfileComplete(isProfileComplete);

        return new LoginResponse(accessToken, refreshToken, userDto);
    }

    public AccessTokenResponse refreshToken(String refreshToken) {
        if (!tokenGenerator.validateToken(refreshToken, null)) {
            throw new BusinessException("Refresh token inválido ou expirado");
        }

        String email = tokenGenerator.getSubjectFromToken(refreshToken);
        CarWashRegistration carWash = carWashRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Lava-rápido não encontrado com o e-mail: " + email));

        String newAccessToken = tokenGenerator.generateAccessTokenForCarWash(carWash);
        long accessTokenValiditySeconds = accessTokenExpirationMs / 1000;

        return new AccessTokenResponse(newAccessToken, (int) accessTokenValiditySeconds);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Email autenticado: " + email);

        CarWashRegistration carWash = carWashRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Lava-rápido não encontrado"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), carWash.getPassword())) {
            throw new BusinessException("Senha atual incorreta");
        }

        carWash.setPassword(passwordEncoder.encode(request.getNewPassword()));
        carWashRepository.save(carWash);
    }
}