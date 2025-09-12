package com.icar.platform.api.dto.response.admin;

import java.time.LocalDate;
import java.util.UUID;

public record PartnerResponse(
        UUID id,
        String tradeName,
        String ownerName,
        String email,
        String phone,
        String cnpj,
        LocalDate joinDate,
        String status,
        CurrentPlanDto currentPlan,
        String zipCode,
        String city,
        String state
) {
    public record CurrentPlanDto(
            String name,
            LocalDate expiresAt
    ) {}
}