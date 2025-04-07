package com.icar.plataform.api.dto.response;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationResponse {
        private boolean success;
        private String message;
}
