package com.icar.platform.api.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private UserDto user;

    @JsonIgnore
    private String refreshToken;

    public LoginResponse(String accessToken, String refreshToken, UserDto user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }
}