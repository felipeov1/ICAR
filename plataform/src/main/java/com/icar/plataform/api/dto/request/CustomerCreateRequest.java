package com.icar.plataform.api.dto.request;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreateRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
}