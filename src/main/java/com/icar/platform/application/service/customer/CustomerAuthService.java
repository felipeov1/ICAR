package com.icar.platform.application.service.customer;

import com.icar.platform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.platform.api.dto.response.auth.RegisterCustomerResponse;
import com.icar.platform.api.dto.response.auth.EmailVerificationResponse;
import com.icar.platform.shared.exception.BusinessException;

public interface CustomerAuthService {
    RegisterCustomerResponse create(RegisterCustomerRequest request);
    EmailVerificationResponse verifyEmail(String token) throws BusinessException;
}