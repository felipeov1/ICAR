package com.icar.platform.domain.model.ads;

import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "advertisements")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_wash_profile_id")
    private CarWashProfile carWashProfile;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "link_url", nullable = false)
    private String linkUrl;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_platform_ad", nullable = false)
    private boolean isPlatformAd = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}