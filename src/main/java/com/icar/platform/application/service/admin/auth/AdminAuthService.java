package com.icar.platform.application.service.admin.auth;

import com.icar.platform.api.dto.request.auth.LoginRequest;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import com.icar.platform.api.dto.response.auth.UserDto;
import com.icar.platform.domain.model.admin.Admin;
import com.icar.platform.domain.repository.admin.AdminRepository;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import com.icar.platform.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public LoginResponse authenticate(LoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), admin.getPassword())) {
            throw new BusinessException("Credenciais inválidas");
        }

        String accessToken = tokenGenerator.generateAccessTokenForAdmin(admin);
        String refreshToken = tokenGenerator.generateRefreshTokenForAdmin(admin);

        UserDto userDto = new UserDto();
        userDto.setId(admin.getId());
        userDto.setEmail(admin.getEmail());
        userDto.setRole("ADMIN");
        userDto.setFullName(admin.getFullName());

        return new LoginResponse(accessToken, refreshToken, userDto);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (refreshToken == null || !tokenGenerator.validateToken(refreshToken, null)) {
            throw new BusinessException("Refresh token inválido ou expirado");
        }

        String email = tokenGenerator.getSubjectFromToken(refreshToken);

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Administrador não encontrado: " + email));

        String newAccessToken = tokenGenerator.generateAccessTokenForAdmin(admin);
        String newRefreshToken = tokenGenerator.generateRefreshTokenForAdmin(admin);

        UserDto userDto = new UserDto();
        userDto.setId(admin.getId());
        userDto.setEmail(admin.getEmail());
        userDto.setRole("ADMIN");
        userDto.setFullName(admin.getFullName());

        return new LoginResponse(newAccessToken, newRefreshToken, userDto);
    }

    public void logout() {}
}
