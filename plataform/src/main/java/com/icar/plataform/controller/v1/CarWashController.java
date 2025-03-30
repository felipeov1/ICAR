package com.icar.plataform.controller.v1;

import com.icar.plataform.dto.request.CarWashRequest;
import com.icar.plataform.dto.response.CarWashResponse;
import com.icar.plataform.service.CarWashService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Car Washes", description = "Car wash establishment operations")
@RestController
@RequestMapping("/api/v1/carwashes")
@RequiredArgsConstructor
public class CarWashController {

    private final CarWashService service;

    @Operation(summary = "Register new car wash")
    @PostMapping
    public ResponseEntity<CarWashResponse> create(
            @Valid @org.springframework.web.bind.annotation.RequestBody CarWashRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @Operation(summary = "Find car washes nearby")
    @GetMapping("/nearby")
    public ResponseEntity<List<CarWashResponse>> getNearby(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radiusKm) {
        return ResponseEntity.ok(service.findNearby(latitude, longitude, radiusKm));
    }
}