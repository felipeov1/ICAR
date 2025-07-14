package com.icar.platform.api.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailVerificationResponse {
        private String email;
        private String message;
        private boolean success;
}