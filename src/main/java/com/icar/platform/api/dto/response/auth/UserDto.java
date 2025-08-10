package com.icar.platform.api.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private UUID id;
    private UUID profileId;
    private String email;
    private String fullName;
    private String role;
    private Boolean emailVerified;
    private Boolean isProfileComplete;
}