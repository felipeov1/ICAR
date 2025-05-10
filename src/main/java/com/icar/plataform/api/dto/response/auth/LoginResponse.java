// LoginResponse.java
package com.icar.plataform.api.dto.response.auth;

import com.icar.plataform.api.dto.response.customer.CustomerResponse;

public record LoginResponse(
        String token,
        String refreshToken,
        Long expiresIn,
        CustomerResponse customer
) {}