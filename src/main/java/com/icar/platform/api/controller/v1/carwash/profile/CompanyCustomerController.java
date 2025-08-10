package com.icar.platform.api.controller.v1.carwash.profile;

import com.icar.platform.api.dto.request.carwash.profile.CompanyCustomerRequest;
import com.icar.platform.api.dto.response.carwash.profile.CompanyCustomerResponse;
import com.icar.platform.application.service.carwash.profile.CompanyCustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Car Wash - Company Customers")
@RestController
@RequestMapping("/api/v1/profile/{profileId}/customers")
@RequiredArgsConstructor
public class CompanyCustomerController {

    private final CompanyCustomerService customerService;

    @PostMapping
    public ResponseEntity<CompanyCustomerResponse> createCustomer(@PathVariable UUID profileId, @Valid @RequestBody CompanyCustomerRequest request) {
        return ResponseEntity.status(201).body(customerService.create(profileId, request));
    }

    @GetMapping
    public ResponseEntity<List<CompanyCustomerResponse>> getCustomers(@PathVariable UUID profileId) {
        return ResponseEntity.ok(customerService.findByProfile(profileId));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CompanyCustomerResponse> updateCustomer(
            @PathVariable UUID profileId,
            @PathVariable UUID customerId,
            @Valid @RequestBody CompanyCustomerRequest request) {

        CompanyCustomerResponse updatedCustomer = customerService.update(profileId, customerId, request);
        return ResponseEntity.ok(updatedCustomer);
    }
}
