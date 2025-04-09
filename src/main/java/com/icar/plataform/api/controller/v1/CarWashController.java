package com.icar.plataform.api.controller.v1;

import com.icar.plataform.api.dto.request.CarWashRequest;
import com.icar.plataform.api.dto.response.CarWashResponse;
import com.icar.plataform.application.service.carwash.CarWashService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Car Washes", description = "Car wash establishment operations")
@RestController
@RequestMapping("/v1/carwashes")
@RequiredArgsConstructor
public class CarWashController {

    private final CarWashService service;

    @Operation(summary = "Register new car wash")
    @PostMapping
    public ResponseEntity<CarWashResponse> create(
            @Valid @org.springframework.web.bind.annotation.RequestBody CarWashRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @Operation(summary = "List all Car Washes ordered by creation date")
    @GetMapping("/all")
    public ResponseEntity<List<CarWashResponse>> findAll() {
        return ResponseEntity.ok(service.findAllByOrderByCreatedAtAsc());
    }
}