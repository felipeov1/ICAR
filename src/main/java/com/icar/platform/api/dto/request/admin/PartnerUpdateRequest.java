package com.icar.platform.api.dto.request.admin;

import java.time.LocalDate;
import java.util.UUID;

public record PartnerUpdateRequest(
        String tradeName,
        String ownerName,
        String cnpj,
        String phone,
        String email,
        String zipCode,
        String city,
        String state,
        UUID planId,
        LocalDate joinDate
) {}