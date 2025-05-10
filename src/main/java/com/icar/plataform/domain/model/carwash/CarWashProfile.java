package com.icar.plataform.domain.model.carwash;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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

    @Column(name = "car_wash_id", nullable = false)
    private UUID carWashId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "cover_photo", length = 255)
    private String coverPhoto;

    @ElementCollection
    @CollectionTable(name = "car_wash_profile_photos", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    @ElementCollection
    @CollectionTable(name = "car_wash_profile_opening_hours", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "opening_hour")
    private List<String> openingHours;

    @ElementCollection
    @CollectionTable(name = "car_wash_profile_vehicle_types", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "vehicle_type")
    private List<String> vehicleTypes;

    @OneToMany(mappedBy = "carWashProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarWashOffering> offerings;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

