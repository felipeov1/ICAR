package com.icar.plataform.domain.model.carwash.profile;

import com.icar.plataform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.plataform.domain.model.carwash.offering.CarWashOffering;
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

    @OneToOne
    @JoinColumn(name = "car_wash_id", nullable = false, unique = true)
    private CarWashRegistration carWashRegistration;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "cover_photo", length = 255)
    private String coverPhoto;

    @ElementCollection
    @CollectionTable(
            name = "car_wash_profile_photos",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "photo_url", length = 255)
    private List<String> photos;

    @ElementCollection
    @CollectionTable(
            name = "car_wash_profile_vehicle_types",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "vehicle_type", length = 100)
    private List<String> vehicleTypes;

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
}