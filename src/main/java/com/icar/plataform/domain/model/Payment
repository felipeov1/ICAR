package com.icar.plataform.domain.model;

import com.icar.plataform.domain.enums.PaymentMethodType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(name = "payment_intent_id", nullable = false, length = 255)
    private String paymentIntentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethodType paymentMethod;

    @Column(name = "payment_status", nullable = false, length = 50)
    private String paymentStatus;

    @Column(nullable = false, columnDefinition = "NUMERIC(10,2)")
    private BigDecimal amount;

    @Column(length = 10)
    private String currency = "BRL";

    @Column(name = "stripe_fee", columnDefinition = "NUMERIC(10,2)")
    private BigDecimal stripeFee;

    @Column(name = "pix_code", columnDefinition = "TEXT")
    private String pixCode;

    @Column(name = "pix_expiration")
    private LocalDateTime pixExpiration;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}