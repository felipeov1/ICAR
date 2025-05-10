package com.icar.plataform.domain.model.carwash;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "car_wash_registration")
public class CarWashRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cnpj", length = 14, nullable = true)
    private String cnpj;

    @Column(name = "cpf", length = 11, nullable = true)
    private String cpf;

    @Column(name = "legal_name", nullable = false, length = 255)
    private String legal_name;

    @Column(name = "trade_name", length = 255)
    private String trade_name;

    @Column(name = "owner_name", nullable = false, length = 255)
    private String ownerName;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "address", nullable = false, length = 255)
    private String address;
}
