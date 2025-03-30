package com.icar.plataform.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
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
    @JoinColumn(name = "car_wash_id", nullable = false)
    private CarWash carWash;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(columnDefinition = "NUMERIC(10,8)")
    private BigDecimal latitude;

    @Column(columnDefinition = "NUMERIC(11,8)")
    private BigDecimal longitude;

    @Column(name = "cover_photo", length = 255)
    private String coverPhoto;

    @JdbcTypeCode(SqlTypes.JSON)
    private String photos;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "opening_hours")
    private String openingHours;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "vehicle_types")
    private String vehicleTypes;
}
