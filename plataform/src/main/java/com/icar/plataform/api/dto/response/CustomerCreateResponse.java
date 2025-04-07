package com.icar.plataform.api.dto.response;

import com.icar.plataform.domain.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreateResponse {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private UserStatus status;
}