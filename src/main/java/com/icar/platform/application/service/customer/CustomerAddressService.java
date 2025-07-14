package com.icar.platform.application.service.customer;

import com.icar.platform.api.dto.request.customer.CustomerAddressRequest;
import com.icar.platform.api.dto.response.customer.CustomerAddressResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerAddressService {
    CustomerAddressResponse addAddress(UUID customerId, CustomerAddressRequest request);
    CustomerAddressResponse updateAddress(UUID addressId, CustomerAddressRequest request);
    void softDeleteAddress(UUID customerId, UUID addressId);
    List<CustomerAddressResponse> getAllAddresses(UUID customerId);
}

