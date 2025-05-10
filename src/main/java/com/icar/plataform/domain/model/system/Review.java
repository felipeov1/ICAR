package com.icar.plataform.domain.model.system;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private UUID carWashId;

    @Column(nullable = false)
    private UUID serviceId;

    @Column(nullable = false)
    @Min(1) @Max(5)
    private Integer rating;

    private String feedback;

    @CreationTimestamp
    private LocalDateTime createdAt;
}