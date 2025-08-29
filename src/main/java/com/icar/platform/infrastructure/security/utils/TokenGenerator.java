package com.icar.platform.infrastructure.security.utils;

import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
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

    private static final Logger logger = LoggerFactory.getLogger(TokenGenerator.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.access-token}")
    private long accessTokenExpiration;

    @Value("${jwt.expiration.refresh-token}")
    private long refreshTokenExpirationMs;


    public String generateAccessToken(Customer customer) {
        return generateToken(customer, accessTokenExpiration);
    }

    public String generateRefreshToken(Customer customer) {
        return generateToken(customer, refreshTokenExpirationMs);
    }

    public String generateTokenForCarWash(CarWashRegistration carWash, long expirationTimeMillis) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", carWash.getId());
        claims.put("email", carWash.getEmail());
        claims.put("tradeName", carWash.getLegalName());
        claims.put("role", "CARWASH");
        claims.put("profileId", carWash.getProfileId());
        claims.put("isProfileComplete", carWash.isProfileComplete());

        return createToken(claims, carWash.getEmail(), expirationTimeMillis);
    }

    public String generateToken(Customer customer, long expirationTimeMillis) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", customer.getId());
        claims.put("email", customer.getEmail());
        claims.put("fullName", customer.getFullName());
        claims.put("role", "CUSTOMER");
        claims.put("emailVerified", customer.isEmailVerified());

        return createToken(claims, customer.getEmail(), expirationTimeMillis);
    }

    public String generateAccessTokenForCarWash(CarWashRegistration carWash) {
        return generateTokenForCarWash(carWash, accessTokenExpiration);
    }

    public String generateRefreshTokenForCarWash(CarWashRegistration carWash) {
        return generateTokenForCarWash(carWash, refreshTokenExpirationMs);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTimeMillis) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
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

    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getRoleFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("role");
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
                String tokenSubject = getSubjectFromToken(token);
                if (!tokenSubject.equals(userDetails.getUsername())) {
                    logger.warn("Token subject {} doesn't match user {}", tokenSubject, userDetails.getUsername());
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