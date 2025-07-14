package com.icar.platform.domain.model.appointment;

import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.enums.PaymentMethod;
import com.icar.platform.domain.model.carwash.offering.CarWashOffering;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.customer.Customer;
import com.icar.platform.domain.model.customer.CustomerAddress;
import com.icar.platform.domain.model.payment.coupon.AppliedCoupon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    private ZonedDateTime dateTime;

    @Column(name = "original_amount", precision = 10, scale = 2)
    private BigDecimal originalAmount;

    @Column(name = "amount_paid", precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20, nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "total_duration_minutes")
    private Integer totalDurationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppliedCoupon> appliedCoupons = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "car_wash_profile_appointment_selected_extras",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "extra_service_id")
    )
    private Set<CarWashOffering> selectedExtraServices;
}