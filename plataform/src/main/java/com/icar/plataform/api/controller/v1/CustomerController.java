package com.icar.plataform.api.controller.v1;

import com.icar.plataform.api.dto.request.CustomerCreateRequest;
import com.icar.plataform.api.dto.response.CustomerCreateResponse;
import com.icar.plataform.application.service.customer.CustomerService;
import com.icar.plataform.infrastructure.validation.exception.CustomValidationException;
import com.icar.plataform.shared.exception.BusinessException;
import com.icar.plataform.shared.exception.ValidationException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customers", description = "Customers management operations")
@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody CustomerCreateRequest request) { // Sem @Valid
        CustomerCreateResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}