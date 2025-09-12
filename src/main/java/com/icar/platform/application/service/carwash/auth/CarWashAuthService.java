package com.icar.platform.application.service.carwash.auth;

import com.icar.platform.api.dto.request.auth.LoginRequest;
import com.icar.platform.api.dto.request.carwash.auth.ChangePasswordRequest;
import com.icar.platform.api.dto.response.auth.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CarWashAuthService {

    LoginResponse authenticate(LoginRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    LoginResponse refreshToken(String refreshToken);

    void changePassword(ChangePasswordRequest request);

    void logout();
}