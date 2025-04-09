package com.icar.plataform.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "car_wash_closure")
public class CarWashClosure {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "car_wash_id", nullable = false)
    private CarWash carWash;

    @Column(name = "closure_date", nullable = false)
    private LocalDate closureDate;

    @Column(length = 255)
    private String reason;
}