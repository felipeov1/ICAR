package com.icar.platform.api.controller.v1.admin.partner;

import com.icar.platform.api.dto.request.admin.PartnerCreateRequest;
import com.icar.platform.api.dto.request.admin.PartnerUpdateRequest;
import com.icar.platform.api.dto.response.admin.PartnerResponse;
import com.icar.platform.application.service.admin.partner.PartnerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/partners")
@RequiredArgsConstructor
@Tag(name = "Admin: Partner Management")
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<List<PartnerResponse>> getAllPartners() {
        return ResponseEntity.ok(partnerService.findAllPartners());
    }

    @PostMapping
    public ResponseEntity<PartnerResponse> createPartner(@RequestBody PartnerCreateRequest request) {
        return new ResponseEntity<>(partnerService.createPartner(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerResponse> updatePartner(@PathVariable UUID id, @RequestBody PartnerUpdateRequest request) {
        return ResponseEntity.ok(partnerService.updatePartner(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivatePartner(@PathVariable UUID id) {
        partnerService.deactivatePartner(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/confirm-payment")
    public ResponseEntity<PartnerResponse> confirmPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(partnerService.renewSubscriptionForPartner(id));
    }
}