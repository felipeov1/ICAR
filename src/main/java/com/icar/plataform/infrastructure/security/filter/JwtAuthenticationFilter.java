// JwtAuthenticationFilter.java
package com.icar.plataform.infrastructure.security.filter;

import com.icar.plataform.infrastructure.security.service.CustomerDetailsService;
import com.icar.plataform.infrastructure.security.utils.TokenGenerator;
import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@NonNullApi
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenGenerator tokenGenerator;
    private final CustomerDetailsService customerDetailsService;

    public JwtAuthenticationFilter(TokenGenerator tokenGenerator,
                                   CustomerDetailsService customerDetailsService) {
        this.tokenGenerator = tokenGenerator;
        this.customerDetailsService = customerDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                // Primeiro valida a estrutura e expiração do token
                if (!tokenGenerator.validateToken(jwt, null)) {
                    logger.warn("Token structure invalid or expired");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                String email = tokenGenerator.getEmailFromToken(jwt);
                UserDetails userDetails = customerDetailsService.loadUserByUsername(email);

                // Depois valida contra os detalhes do usuário
                if (!tokenGenerator.validateToken(jwt, userDetails)) {
                    logger.warn("Token doesn't match user details");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Authentication failed", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Nullable
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
