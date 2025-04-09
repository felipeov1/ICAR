package com.icar.plataform.domain.model;

import com.icar.plataform.domain.enums.ServiceModality;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "NUMERIC(10,2)")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "car_wash_id", nullable = false)
    private CarWash carWash;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ServiceModality modality;


    @Column(name = "estimated_time")
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    private Duration estimatedTime;

    @Column(name = "materials_needed", columnDefinition = "TEXT")
    private String materialsNeeded;
}