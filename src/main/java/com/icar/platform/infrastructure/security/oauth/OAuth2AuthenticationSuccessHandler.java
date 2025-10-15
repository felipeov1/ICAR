package com.icar.platform.infrastructure.security.oauth;

import com.icar.platform.application.service.oauth.OAuth2CustomerService;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import com.icar.platform.shared.exception.BusinessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2CustomerService oAuth2CustomerService;
    private final TokenGenerator tokenGenerator;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String targetUrl;

        try {
            Customer customer = oAuth2CustomerService.processOAuth2User(oauth2User);
            String accessToken = tokenGenerator.generateAccessToken(customer);
            String refreshToken = tokenGenerator.generateRefreshToken(customer);

            targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                    .queryParam("accessToken", accessToken)
                    .queryParam("refreshToken", refreshToken)
                    .build().toUriString();

        } catch (BusinessException ex) {
            targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/entrar")
                    .queryParam("error", "email_exists_local")
                    .build().toUriString();
        }

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}