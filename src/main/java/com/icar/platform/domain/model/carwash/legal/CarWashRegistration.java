package com.icar.platform.domain.model.carwash.legal;

import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "car_wash_registration")
@FilterDef(name = "notDeleted", parameters = @ParamDef(name = "isNull", type = Boolean.class))
@Filter(name = "notDeleted", condition = "deleted_at IS NULL")
public class CarWashRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cnpj", length = 14, unique = true, nullable = true)
    private String cnpj;

    @Column(name = "cpf", length = 11, unique = true, nullable = true)
    private String cpf;

    @Column(name = "legal_name", nullable = false, length = 255)
    private String legalName;

    @Column(name = "trade_name", length = 255)
    private String tradeName;

    @Column(name = "owner_name", nullable = false, length = 255)
    private String ownerName;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number")
    private String number;

    @Column(name = "complement")
    private String complement;

    @Column(name = "neighborhood", nullable = false)
    private String neighborhood;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false, length = 2)
    private String state;

    @Column(name = "zip_code", nullable = false, length = 9)
    private String zipCode;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToOne(mappedBy = "carWashRegistration", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CarWashProfile profile;

    public UUID getProfileId() {
        return profile != null ? profile.getId() : null;
    }

    public boolean isProfileComplete() {
        return profile != null && profile.isComplete();
    }
}

