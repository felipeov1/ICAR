package com.icar.platform.application.service.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.CompanyCustomerRequest;
import com.icar.platform.api.dto.response.carwash.profile.CompanyCustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CompanyCustomerService {
    CompanyCustomerResponse create(UUID profileId, CompanyCustomerRequest request);
    List<CompanyCustomerResponse> findByProfile(UUID profileId);
    CompanyCustomerResponse update(UUID profileId, UUID customerId, CompanyCustomerRequest request);

}