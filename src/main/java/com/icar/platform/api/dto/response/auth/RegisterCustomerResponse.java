package com.icar.platform.api.dto.response.auth;

import com.icar.platform.domain.enums.UserStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCustomerResponse {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private UserStatus status;
}