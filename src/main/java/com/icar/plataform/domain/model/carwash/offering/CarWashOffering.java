package com.icar.plataform.domain.model.carwash.offering;

import com.icar.plataform.domain.enums.CarWashOfferingModality;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "car_wash_offering")
@Getter
@Setter
public class CarWashOffering {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private CarWashProfile profile;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 100)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "estimated_time", nullable = false)
    private Integer estimatedTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarWashOfferingModality modality;

    @Column(nullable = false)
    private BigDecimal price;
}
