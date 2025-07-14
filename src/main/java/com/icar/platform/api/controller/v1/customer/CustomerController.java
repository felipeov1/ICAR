package com.icar.platform.api.controller.v1.customer;

import com.icar.platform.api.dto.request.customer.*;
import com.icar.platform.api.dto.response.customer.*;
import com.icar.platform.application.service.customer.CustomerProfileService;
import com.icar.platform.application.service.customer.CustomerAddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerProfileService profileService;
    private final CustomerAddressService addressService;

    @Operation(summary = "Get customer profile", tags = {"Customer Settings - Profile"})
    @GetMapping("/{customerId}/profile")
    public ResponseEntity<CustomerProfileResponse> getProfile(
            @PathVariable UUID customerId) {
        return ResponseEntity.ok(profileService.getProfile(customerId));
    }

    @Operation(summary = "Update profile", tags = {"Customer Settings - Profile"})
    @PutMapping("/{customerId}/profile")
    public ResponseEntity<CustomerProfileResponse> updateProfile(
            @PathVariable UUID customerId,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(customerId, request));
    }

    @Operation(summary = "Change password", tags = {"Customer Settings - Profile"})
    @PatchMapping("/{customerId}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable UUID customerId,
            @Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(customerId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add address", tags = {"Customer Settings - Addresses"})
    @PostMapping("/{customerId}/addresses")
    public ResponseEntity<CustomerAddressResponse> addAddress(
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerAddressRequest request) {
        return ResponseEntity.ok(addressService.addAddress(customerId, request));
    }

    @Operation(summary = "Get all addresses", tags = {"Customer Settings - Addresses"})
    @GetMapping("/{customerId}/addresses")
    public ResponseEntity<List<CustomerAddressResponse>> getAddresses(
            @PathVariable UUID customerId) {
        return ResponseEntity.ok(addressService.getAllAddresses(customerId));
    }

    @Operation(summary = "Update address", tags = {"Customer Settings - Addresses"})
    @PutMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<CustomerAddressResponse> updateAddress(
            @PathVariable UUID customerId,
            @PathVariable UUID addressId,
            @Valid @RequestBody CustomerAddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(addressId, request));
    }
}