package com.icar.platform.config;

import com.icar.platform.infrastructure.security.filter.JwtAuthenticationFilter;
import com.icar.platform.infrastructure.security.oauth.OAuth2AuthenticationSuccessHandler;
import com.icar.platform.infrastructure.security.service.CustomerDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomerDetailsService customerDetailsService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**",
            "/swagger-resources/**", "/favicon.ico"
    };

    public static final String[] PUBLIC_WHITELIST = {
            "/api/v1/auth/**",
            "/api/v1/carwash/auth/**",
            "/api/v1/admin/auth/**",
            "/login/**",
            "/oauth2/**",
            "/api/v1/health",
            "/error",
            "/uploads/**",
            "/api/v1/notifications/mercado-pago",
            "/api/v1/mercado-pago/**",
    };

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/entrar?error=oauth_failed");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers(PUBLIC_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/marketplace/carwashes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/profile/subdomain/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/profile/*/vehicle-types").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/profile/*/offerings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/profile/*/photos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/carwashes/*/reviews").permitAll()
                        .requestMatchers("/api/v1/push/subscribe", "/api/v1/push/unsubscribe").authenticated()
                        .requestMatchers("/api/v1/push/**").authenticated()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/*"))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler())
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:5173", "http://*.localhost:5173", "http://192.168.3.8:5173",
                "https://localhost:5173", "https://*.localhost:5173", "https://192.168.3.8:5173",
                "https://icarplus.com.br", "https://*.icarplus.com.br"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}