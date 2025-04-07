package com.icar.plataform.application.service.customer;

import com.icar.plataform.api.dto.request.CustomerCreateRequest;
import com.icar.plataform.api.dto.response.CustomerCreateResponse;

public interface CustomerService {
    CustomerCreateResponse create(CustomerCreateRequest request);
}