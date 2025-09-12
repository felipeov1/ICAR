package com.icar.platform.domain.model.advertisement;

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
    @Column(nullable = true)
    private String title;
    @Column(length = 1000, nullable = true)
    private String description;
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    @Column(name = "link_url", nullable = true)
    private String linkUrl;
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    @Column(name = "is_platform_ad", nullable = false)
    private boolean isPlatformAd = false;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
    @Column(name = "expires_at", nullable = true)
    private LocalDateTime expiresAt;
}