package com.icar.platform.domain.model.carwash.profile;

import com.icar.platform.domain.model.appointment.CarWashAppointment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "company_customer")
public class CompanyCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private CarWashProfile profile;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column
    private String phone;

    @Column(name = "zip_code")
    private String zipCode;

    private String street;

    @Column(name = "street_number")
    private String streetNumber;

    private String neighborhood;
    private String city;
    private String state;

    @Column(name = "additional_instructions")
    private String additionalInstructions;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(
            mappedBy = "companyCustomer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CarWashAppointment> appointments;
}