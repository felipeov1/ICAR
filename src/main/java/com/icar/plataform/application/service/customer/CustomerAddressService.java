package com.icar.plataform.application.service.customer;

import com.icar.plataform.api.dto.request.CustomerAddressRequest;
import com.icar.plataform.api.dto.response.CustomerAddressResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerAddressService {
    CustomerAddressResponse addAddress(UUID customerId, CustomerAddressRequest request);
    CustomerAddressResponse updateAddress(UUID addressId, CustomerAddressRequest request);
    void softDeleteAddress(UUID customerId, UUID addressId);
    List<CustomerAddressResponse> getAllAddresses(UUID customerId);
}

