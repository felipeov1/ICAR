package com.icar.platform.api.dto.request.carwash;


public record CarWashRegistrationRequest(
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