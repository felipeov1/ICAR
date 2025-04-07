package com.icar.plataform.api.controller.v1;

import com.icar.plataform.api.dto.request.CustomerCreateRequest;
import com.icar.plataform.api.dto.response.CustomerCreateResponse;
import com.icar.plataform.api.dto.response.EmailVerificationResponse;
import com.icar.plataform.application.service.customer.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customers", description = "Customers management operations")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/registrar")
    public ResponseEntity<?> create(@RequestBody @Valid CustomerCreateRequest request) {
        CustomerCreateResponse response = customerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/confirmacao/{token}")
    public ResponseEntity<?> confirmEmail(@PathVariable String token) {
        EmailVerificationResponse response = customerService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }
}