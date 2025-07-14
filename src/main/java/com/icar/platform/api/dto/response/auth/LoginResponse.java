package com.icar.platform.api.dto.response.auth;

import com.icar.platform.api.dto.response.customer.CustomerResponse;

public record LoginResponse(
        String token,
        String refreshToken,
        Long expiresIn,
        CustomerResponse customer
) {}