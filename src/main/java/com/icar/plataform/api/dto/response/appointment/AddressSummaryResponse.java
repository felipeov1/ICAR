package com.icar.plataform.api.dto.response.appointment;

import java.util.UUID;

public record AddressSummaryResponse(
        UUID id,
        String street,
        String getStreetNumber,
        String city
) {}
