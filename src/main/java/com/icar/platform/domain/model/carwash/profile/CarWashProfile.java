package com.icar.platform.domain.model.carwash.profile;

import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "car_wash_profile")
public class CarWashProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_wash_id", nullable = false, unique = true)
    private CarWashRegistration carWashRegistration;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "rating", nullable = false, precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "reviews", nullable = false)
    private Integer reviews;

    @Column(name = "modalities", columnDefinition = "text[]")
    private String[] modalities;

    @Column(name = "logo", length = 255)
    private String logo;

    @Column(name = "cover_photo", length = 255)
    private String coverPhoto;

    @Column(unique = true, nullable = false, length = 100)
    private String subdomain;

    @Column(length = 20)
    private String whatsapp;

    @Column(name = "locations", columnDefinition = "text[]")
    private String[] locations;

    @Column(name = "wet_wash_observations", length = 500)
    private String wetWashObservations;

    @Column(name = "dry_wash_observations", length = 500)
    private String dryWashObservations;

    @Column(length = 500)
    @Deprecated
    private String observations;

    @Column(name = "is_onboarding_complete", nullable = false)
    private boolean isOnboardingComplete = false;

    @ElementCollection
    @CollectionTable(
            name = "car_wash_profile_photos",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "photo_url", length = 255)
    private List<String> photos = new ArrayList<>();

    @OneToMany(
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<CarWashOffering> offerings;

    @OneToMany(
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<WeeklySchedule> weeklySchedules;

    @OneToMany(
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<SpecialDay> specialDays;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean isComplete() {
        return name != null && !name.isEmpty() &&
                whatsapp != null && !whatsapp.isEmpty() &&
                subdomain != null && !subdomain.isEmpty() &&
                isOnboardingComplete;
    }
}