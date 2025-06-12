package com.icar.plataform.domain.model.appointment;

import com.icar.plataform.domain.enums.AppointmentStatus;
import com.icar.plataform.domain.enums.PaymentMethod;
import com.icar.plataform.domain.model.carwash.offering.CarWashOffering;
import com.icar.plataform.domain.model.carwash.profile.CarWashProfile;
import com.icar.plataform.domain.model.customer.Customer;
import com.icar.plataform.domain.model.customer.CustomerAddress;
import com.icar.plataform.domain.model.payment.coupon.AppliedCoupon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "car_wash_appointment")
public class CarWashAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private CarWashProfile profile;

    @Column(nullable = false, length = 50)
    private String carType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id", nullable = false)
    private CarWashOffering offering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private CustomerAddress address;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "amount_paid", precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20, nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppliedCoupon> appliedCoupons = new ArrayList<>();

}