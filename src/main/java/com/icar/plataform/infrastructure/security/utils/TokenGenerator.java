package com.icar.plataform.infrastructure.security.utils;

import com.icar.plataform.domain.model.customer.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.lang.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class TokenGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TokenGenerator.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public String generateAccessToken(Customer customer) {
        return buildToken(customer, 3600000); // 1 hora
    }

    public String generateRefreshToken(Customer customer) {
        return buildToken(customer, 2592000000L); // 30 dias
    }

    private String buildToken(Customer customer, long expirationMs) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", customer.getId());
        claims.put("email", customer.getEmail());
        claims.put("role", "CUSTOMER");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(customer.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Customer customer, long expirationTimeMillis) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", customer.getId());
            claims.put("email", customer.getEmail());
            claims.put("fullName", customer.getFullName());
            claims.put("role", "CUSTOMER");

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(customer.getEmail())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("Failed to generate token", e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build();
            return parser.parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.error("Failed to parse token claims", e);
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token, @Nullable UserDetails userDetails) {
        try {
            if (isTokenExpired(token)) {
                logger.warn("Token is expired");
                return false; // token inválido
            }

            if (userDetails != null) {
                String tokenEmail = getEmailFromToken(token);
                if (!tokenEmail.equals(userDetails.getUsername())) {
                    logger.warn("Token email mismatch");
                    return false; // token inválido
                }
            }
            return true; // token válido
        } catch (Exception e) {
            logger.error("Token validation failed", e);
            return false; // token inválido
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


}