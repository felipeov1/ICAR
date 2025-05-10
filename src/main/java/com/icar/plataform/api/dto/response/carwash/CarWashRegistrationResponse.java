package com.icar.plataform.api.dto.response.carwash;

import java.util.UUID;


public record CarWashRegistrationResponse(
        UUID id,
        String cnpj,
        String cpf,
        String legal_name,
        String trade_name,
        String ownerName,
        String phone,
        String email,
        String address
) {}