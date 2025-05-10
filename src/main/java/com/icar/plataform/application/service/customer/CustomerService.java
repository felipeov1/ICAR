package com.icar.plataform.application.service.customer;

import com.icar.plataform.api.dto.request.auth.RegisterCustomerRequest;
import com.icar.plataform.api.dto.response.auth.RegisterCustomerResponse;
import com.icar.plataform.api.dto.response.auth.EmailVerificationResponse;

public interface CustomerService {
    RegisterCustomerResponse create(RegisterCustomerRequest request);
    EmailVerificationResponse verifyEmail(String token);
}