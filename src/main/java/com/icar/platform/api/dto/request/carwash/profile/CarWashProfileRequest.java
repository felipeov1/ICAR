package com.icar.platform.api.dto.request.carwash.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarWashProfileRequest {

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String name;

    @Size(max = 1000, message = "A descrição não pode exceder 1000 caracteres.")
    private String description;

    @NotNull(message = "A lista de modalidades não pode ser nula.")
    private String[] modalities;

    @NotBlank(message = "O subdomínio não pode estar em branco.")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "O subdomínio pode conter apenas letras minúsculas, números e hifens.")
    @Size(min = 3, max = 50, message = "O subdomínio deve ter entre 3 e 50 caracteres.")
    private String subdomain;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "O número de WhatsApp deve conter apenas dígitos (10 ou 11).")
    private String whatsapp;

    @NotNull(message = "A lista de locais não pode ser nula.")
    private String[] locations;

    @Size(max = 500, message = "As observações não podem exceder 500 caracteres.")
    private String observations;
}