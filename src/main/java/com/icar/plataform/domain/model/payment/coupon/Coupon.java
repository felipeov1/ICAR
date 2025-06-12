package com.icar.plataform.domain.model.payment.coupon;

import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private CarWashProfile profile;

    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;

    @Column(name = "max_uses")
    private Integer maxUses;

    @Column(name = "max_uses_per_user")
    private Integer maxUsesPerUser;

    @Column(name = "current_uses", nullable = false)
    private Integer currentUses = 0;

    @Column(name = "min_order_value", precision = 10, scale = 2)
    private BigDecimal minOrderValue;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}