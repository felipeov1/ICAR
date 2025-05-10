package com.icar.plataform.api.controller.v1.customer;

import com.icar.plataform.api.dto.request.customer.CustomerAddressRequest;
import com.icar.plataform.api.dto.response.customer.CustomerAddressResponse;
import com.icar.plataform.application.service.customer.CustomerAddressService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/addresses")
@RequiredArgsConstructor
public class CustomerAddressController {

    private final CustomerAddressService service;

    @PostMapping
    public ResponseEntity<CustomerAddressResponse> addAddress(
            @PathVariable UUID customerId,
            @RequestBody @Valid CustomerAddressRequest request) {
        return ResponseEntity.ok(service.addAddress(customerId, request));
    }

    @GetMapping
    public ResponseEntity<List<CustomerAddressResponse>> getAllAddresses(
            @PathVariable UUID customerId) {
        return ResponseEntity.ok(service.getAllAddresses(customerId));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<CustomerAddressResponse> updateAddress(
            @PathVariable UUID addressId,
            @RequestBody @Valid CustomerAddressRequest request) {
        return ResponseEntity.ok(service.updateAddress(addressId, request));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable UUID customerId,
            @PathVariable UUID addressId) {
        service.softDeleteAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }
}
