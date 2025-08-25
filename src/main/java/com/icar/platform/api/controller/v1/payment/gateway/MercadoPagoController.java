package com.icar.platform.api.controller.v1.payment.gateway;

import com.icar.platform.application.service.payment.gateway.MercadoPagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.UUID;

@RestController
@Tag(name = "Payments")
@RequestMapping("/api/v1/mercado-pago")
@RequiredArgsConstructor
public class MercadoPagoController {

    private final MercadoPagoService mercadoPagoService;

    @Value("${app.gestao-url}")
    private String gestaoUrl;

    @GetMapping("/authorize")
    @Operation(summary = "Gera a URL de autorização do Mercado Pago para conectar uma conta de vendedor.")
    public RedirectView authorize(@RequestParam UUID companyId) {
        String authorizationUrl = mercadoPagoService.createAuthorizationUrl(companyId);
        return new RedirectView(authorizationUrl);
    }

    @GetMapping("/callback")
    @Operation(summary = "Endpoint de callback para o Mercado Pago após autorização do vendedor.")
    public void handleCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws IOException {
        try {
            mercadoPagoService.exchangeCodeForCredentials(code, state);
            response.sendRedirect(gestaoUrl + "/integrations/mercado-pago/success");
        } catch (Exception e) {
            response.sendRedirect(gestaoUrl + "/integrations/mercado-pago/error?message=" + e.getMessage());
        }
    }
}