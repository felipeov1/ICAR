package com.icar.plataform.controller.v1;

import com.icar.plataform.dto.request.CustomerRequest;
import com.icar.plataform.dto.response.AppointmentResponse;
import com.icar.plataform.dto.response.CustomerResponse;
import com.icar.plataform.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Customers", description = "Customer management operations")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @Operation(summary = "Register new customer")
    @PostMapping
    public ResponseEntity<CustomerResponse> create(
            @Valid @org.springframework.web.bind.annotation.RequestBody
            CustomerRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @Operation(summary = "Get customer's appointment history")
    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAppointments(
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.findAppointmentsByCustomer(id));
    }
}