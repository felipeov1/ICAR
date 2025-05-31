package com.icar.plataform.api.dto.response.carwash;

import java.util.UUID;


public record CarWashRegistrationResponse(
        UUID id,
        String cnpj,
        String cpf,
        String legalName,
        String tradeName,
        String ownerName,
        String phone,
        String email,
        String address,
        String subdomain
) {}
