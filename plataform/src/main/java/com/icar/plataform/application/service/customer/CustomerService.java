package com.icar.plataform.application.service.customer;

import com.icar.plataform.api.dto.request.RegisterCustomerRequest;
import com.icar.plataform.api.dto.response.RegisterCustomerResponse;
import com.icar.plataform.api.dto.response.EmailVerificationResponse;

public interface CustomerService {
    RegisterCustomerResponse create(RegisterCustomerRequest request);
    EmailVerificationResponse verifyEmail(String token);
}