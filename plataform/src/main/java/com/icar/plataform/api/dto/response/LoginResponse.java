// LoginResponse.java
package com.icar.plataform.api.dto.response;

public record LoginResponse(
        String token,
        String refreshToken,
        Long expiresIn,
        CustomerResponse customer
) {}