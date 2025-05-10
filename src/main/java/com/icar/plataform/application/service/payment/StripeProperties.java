package com.icar.plataform.application.service.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
public record StripeProperties(
        String apiKey,
        String successUrl,
        String cancelUrl
) {}
