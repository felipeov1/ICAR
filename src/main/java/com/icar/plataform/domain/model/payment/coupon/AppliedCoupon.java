package com.icar.plataform.domain.model.payment.coupon;

import com.icar.plataform.domain.model.appointment.CarWashAppointment;
import com.icar.plataform.domain.model.customer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "applied_coupons")
public class AppliedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private CarWashAppointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "original_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal originalAmount;

    @Column(name = "final_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalAmount;

    @Column(name = "discount_applied", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountApplied;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;
}