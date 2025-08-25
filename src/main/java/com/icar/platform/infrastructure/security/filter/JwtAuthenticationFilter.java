package com.icar.platform.infrastructure.security.filter;

import com.icar.platform.infrastructure.security.service.CarWashDetailsService;
import com.icar.platform.infrastructure.security.service.CustomerDetailsService;
import com.icar.platform.infrastructure.security.utils.TokenGenerator;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@NonNullApi
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final TokenGenerator tokenGenerator;
    private final CustomerDetailsService customerDetailsService;
    private final CarWashDetailsService carWashDetailsService;

    public JwtAuthenticationFilter(TokenGenerator tokenGenerator,
                                   CustomerDetailsService customerDetailsService,
                                   CarWashDetailsService carWashDetailsService) {
        this.tokenGenerator = tokenGenerator;
        this.customerDetailsService = customerDetailsService;
        this.carWashDetailsService = carWashDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenGenerator.validateToken(jwt, null)) {
                String subject = tokenGenerator.getSubjectFromToken(jwt);
                String role = tokenGenerator.getRoleFromToken(jwt);

                UserDetailsService userDetailsService = getUserDetailsService(role);
                UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Authentication failed", ex);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private UserDetailsService getUserDetailsService(String role) {
        if ("CARWASH".equalsIgnoreCase(role)) {
            return carWashDetailsService;
        }
        return customerDetailsService;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}