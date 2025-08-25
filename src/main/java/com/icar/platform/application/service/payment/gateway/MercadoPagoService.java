package com.icar.platform.application.service.payment.gateway;

import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.model.payment.gateway.CompanyMercadoPagoConfig;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import com.icar.platform.domain.repository.payment.gateway.CompanyMercadoPagoConfigRepository;
import com.icar.platform.shared.exception.BusinessException;
import com.icar.platform.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    @Value("${mercadopago.app-id}")
    private String appId;
    @Value("${mercadopago.client-secret}")
    private String clientSecret;
    @Value("${mercadopago.redirect-uri}")
    private String redirectUri;

    private final CarWashRegistrationDataRepository registrationRepository;
    private final CompanyMercadoPagoConfigRepository configRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String MP_AUTH_URL = "https://auth.mercadopago.com.br/authorization";
    private static final String MP_TOKEN_URL = "https://api.mercadopago.com/oauth/token";

    public String createAuthorizationUrl(UUID companyId) {
        if (!registrationRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Empresa com ID " + companyId + " não encontrada.");
        }

        String state = companyId.toString();

        return MP_AUTH_URL + "?client_id=" + appId +
                "&response_type=code" +
                "&platform_id=mp" +
                "&state=" + state +
                "&redirect_uri=" + redirectUri;
    }

    @Transactional
    public void exchangeCodeForCredentials(String code, String state) {
        log.info("Recebido callback do Mercado Pago com state: {}", state);
        UUID companyId;
        try {
            companyId = UUID.fromString(state);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("O valor de 'state' recebido do Mercado Pago é inválido.");
        }

        CarWashRegistration company = registrationRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa com ID " + companyId + " não encontrada."));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", "application/json");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", appId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(MP_TOKEN_URL, request, Map.class);
            log.info("Credenciais recebidas do Mercado Pago para a empresa {}", companyId);

            saveMercadoPagoConfig(company, Objects.requireNonNull(response));

        } catch (Exception e) {
            log.error("Erro ao trocar código por credenciais do Mercado Pago para a empresa {}: {}", companyId, e.getMessage());
            throw new BusinessException("Falha ao obter credenciais do Mercado Pago. Tente novamente.");
        }
    }

    private void saveMercadoPagoConfig(CarWashRegistration company, Map<String, Object> mpResponse) {
        CompanyMercadoPagoConfig config = configRepository.findByCompanyId(company.getId())
                .orElse(new CompanyMercadoPagoConfig());

        config.setCompany(company);
        String publicKey = (String) mpResponse.get("public_key");
        String receivedAccessToken = (String) mpResponse.get("access_token");
        Long userId = ((Number) mpResponse.get("user_id")).longValue();


        config.setAccessToken(receivedAccessToken);
        config.setPublicKey(publicKey);
        config.setRefreshToken((String) mpResponse.get("refresh_token"));
        config.setUserId(userId);
        config.setExpiresIn(((Number) mpResponse.get("expires_in")).longValue());
        config.setLastRefreshedAt(LocalDateTime.now());
        config.setLiveMode(!publicKey.startsWith("TEST-"));
        configRepository.save(config);
        log.info("Configuração do Mercado Pago salva com sucesso para a empresa {}", company.getId());
    }
}