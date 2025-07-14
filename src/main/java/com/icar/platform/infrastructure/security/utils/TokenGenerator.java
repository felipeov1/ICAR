package com.icar.platform.infrastructure.security.utils;

import com.icar.platform.domain.model.customer.Customer;
import io.jsonwebtoken.*;
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

    @Value("${jwt.expiration.access-token}")
    private long accessTokenExpiration;

    private static final Logger logger = LoggerFactory.getLogger(TokenGenerator.class);

    @Value("${jwt.secret}")
    private String secret;


    public String generateAccessToken(Customer customer) {

        return generateToken(customer, accessTokenExpiration);

    }

    public String generateRefreshToken(Customer customer) {

        return generateToken(customer, 2592000000L);
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
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("Failed to generate token", e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
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
                    .setAllowedClockSkewSeconds(60)
                    .build();
            return parser.parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            logger.error("Failed to parse token claims: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token, @Nullable UserDetails userDetails) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .setAllowedClockSkewSeconds(60)
                    .build();

            parser.parseClaimsJws(token);

            if (userDetails != null) {
                String tokenEmail = getEmailFromToken(token);
                if (!tokenEmail.equals(userDetails.getUsername())) {
                    logger.warn("Token email {} doesn't match user {}", tokenEmail, userDetails.getUsername());
                    return false;
                }
            }
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Token validation failed: JWT has expired (even with clock skew tolerance): {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("Token validation failed: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred during token validation", e);
        }
        return false;
    }

}
