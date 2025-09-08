package com.icar.platform.application.service.carwash.auth;

import com.icar.platform.api.dto.request.auth.LoginRequest;
import com.icar.platform.api.dto.request.carwash.auth.ChangePasswordRequest;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import com.icar.platform.api.dto.response.auth.UserDto;
import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import com.icar.platform.shared.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarWashAuthServiceImpl implements CarWashAuthService {

    private final CarWashRegistrationDataRepository carWashRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    @Override
    public LoginResponse authenticate(LoginRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        CarWashRegistration carWash = carWashRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), carWash.getPassword())) {
            throw new BusinessException("Credenciais inválidas");
        }

        String accessToken = tokenGenerator.generateAccessTokenForCarWash(carWash);
        String refreshToken = tokenGenerator.generateRefreshTokenForCarWash(carWash);

        UserDto userDto = new UserDto();
        userDto.setId(carWash.getId());
        userDto.setEmail(carWash.getEmail());
        userDto.setRole("CARWASH");
        userDto.setProfileId(carWash.getProfile() != null ? carWash.getProfile().getId() : null);
        userDto.setIsProfileComplete(carWash.getProfile() != null && carWash.getProfile().isOnboardingComplete());

        return new LoginResponse(accessToken, refreshToken, userDto);
    }

    @Override // Agora está correto!
    public LoginResponse refreshToken(String refreshToken) {
        if (refreshToken == null || !tokenGenerator.validateToken(refreshToken, null)) {
            throw new BusinessException("Refresh token inválido ou expirado");
        }

        String email = tokenGenerator.getSubjectFromToken(refreshToken);

        CarWashRegistration carWash = carWashRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Lava-rápido não encontrado: " + email));

        String newAccessToken = tokenGenerator.generateAccessTokenForCarWash(carWash);
        String newRefreshToken = tokenGenerator.generateRefreshTokenForCarWash(carWash);

        UserDto userDto = new UserDto();
        userDto.setId(carWash.getId());
        userDto.setEmail(carWash.getEmail());
        userDto.setRole("CARWASH");
        userDto.setProfileId(carWash.getProfile() != null ? carWash.getProfile().getId() : null);
        userDto.setIsProfileComplete(carWash.getProfile() != null && carWash.getProfile().isOnboardingComplete());

        return new LoginResponse(newAccessToken, newRefreshToken, userDto);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = userDetails.getUsername();

        CarWashRegistration carWash = carWashRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), carWash.getPassword())) {
            throw new BusinessException("A senha atual está incorreta.");
        }

        carWash.setPassword(passwordEncoder.encode(request.getNewPassword()));
        carWashRepository.save(carWash);
    }

    @Override
    public void logout() {
    }
}