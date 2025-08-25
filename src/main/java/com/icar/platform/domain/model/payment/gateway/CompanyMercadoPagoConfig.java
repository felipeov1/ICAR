package com.icar.platform.domain.model.payment.gateway;

import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "company_mercado_pago_config")
public class CompanyMercadoPagoConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private CarWashRegistration company;

    @Column(name = "access_token", nullable = false, length = 255)
    private String accessToken;

    @Column(name = "public_key", nullable = false, length = 255)
    private String publicKey;

    @Column(name = "refresh_token", nullable = false, length = 255)
    private String refreshToken;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "live_mode", nullable = false)
    private boolean liveMode;

    @Column(name = "expires_in", nullable = false)
    private long expiresIn;

    @Column(name = "last_refreshed_at")
    private LocalDateTime lastRefreshedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}