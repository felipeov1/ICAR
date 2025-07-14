package com.icar.platform.domain.model.carwash.offering;

import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
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

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;

    @ElementCollection
    @CollectionTable(
            name = "car_wash_offering_vehicle_details",
            joinColumns = @JoinColumn(name = "offering_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"offering_id", "vehicle_type"}
            )
    )
    @MapKeyColumn(name = "vehicle_type", length = 100)
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private Map<String, BigDecimal> vehiclePrices;

    @ElementCollection
    @CollectionTable(
            name = "car_wash_offering_vehicle_details",
            joinColumns = @JoinColumn(name = "offering_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"offering_id", "vehicle_type"}
            )
    )
    @MapKeyColumn(name = "vehicle_type", length = 100)
    @Column(name = "estimated_time", nullable = false)
    private Map<String, Integer> vehicleEstimatedTimes;

}