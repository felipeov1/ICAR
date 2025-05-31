package com.icar.plataform.application.service.customer;

import com.icar.plataform.api.dto.request.customer.*;
import com.icar.plataform.api.dto.response.customer.*;

import java.util.UUID;

public interface CustomerProfileService {
    CustomerProfileResponse getProfile(UUID customerId);
    CustomerProfileResponse updateProfile(UUID customerId, UpdateProfileRequest request);
    void changePassword(UUID customerId, ChangePasswordRequest request);
}