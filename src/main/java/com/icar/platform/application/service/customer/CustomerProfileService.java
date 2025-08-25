package com.icar.platform.application.service.customer;

import com.icar.platform.api.dto.request.customer.*;
import com.icar.platform.api.dto.response.customer.*;

import java.util.UUID;

public interface CustomerProfileService {
    CustomerProfileResponse getProfile(UUID customerId);
    CustomerProfileResponse updateProfile(UUID customerId, UpdateProfileRequest request);
    void changePassword(UUID customerId, ChangePasswordRequest request);
    void updateCustomerCpf(UUID customerId, String cpf);

}