package com.icar.platform.domain.model.carwash.offering;

import com.icar.platform.domain.enums.DirtLevel;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "car_wash_profile_offering")
@SQLDelete(sql = "UPDATE car_wash_profile_offering SET deleted_at = NOW() WHERE id = ?")
public class CarWashOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private CarWashProfile profile;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "service_type", nullable = false)
    private String serviceType;

    @Column(name = "modality", length = 20)
    private String modality;

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "car_wash_offering_vehicle_details",
            joinColumns = @JoinColumn(name = "offering_id")
    )
    @MapKeyColumn(name = "vehicle_type", length = 100)
    private Map<String, VehicleOfferingDetail> vehicleDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "dirt_level_recommendation", nullable = true)
    private DirtLevel dirtLevelRecommendation;
}