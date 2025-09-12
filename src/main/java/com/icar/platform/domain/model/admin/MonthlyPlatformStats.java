package com.icar.platform.domain.model.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "monthly_platform_stats")
@Getter
@Setter
public class MonthlyPlatformStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(name = "gmv_total", nullable = false)
    private BigDecimal gmvTotal = BigDecimal.ZERO;

    @Column(name = "gmv_marketplace", nullable = false)
    private BigDecimal gmvMarketplace = BigDecimal.ZERO;

    @Column(name = "gmv_manual", nullable = false)
    private BigDecimal gmvManual = BigDecimal.ZERO;

    @Column(name = "mrr_total", nullable = false)
    private BigDecimal mrrTotal = BigDecimal.ZERO;

    @Column(name = "mrr_new", nullable = false)
    private BigDecimal mrrNew = BigDecimal.ZERO;

    @Column(name = "active_subscriptions", nullable = false)
    private Integer activeSubscriptions = 0;

    @Column(name = "total_users", nullable = false)
    private Integer totalUsers = 0;

    @Column(name = "new_users", nullable = false)
    private Integer newUsers = 0;

    @Column(name = "total_appointments", nullable = false)
    private Integer totalAppointments = 0;

    @Column(name = "completed_appointments", nullable = false)
    private Integer completedAppointments = 0;

    @Column(name = "canceled_appointments", nullable = false)
    private Integer canceledAppointments = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}