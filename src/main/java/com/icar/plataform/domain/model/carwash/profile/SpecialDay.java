package com.icar.plataform.domain.model.carwash.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "car_wash_profile_special_days")
@SQLDelete(sql = "UPDATE car_wash_profile_special_days SET active = false WHERE id=?")
public class SpecialDay {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private CarWashProfile profile;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime; // null means closed day

    @Column(name = "end_time")
    private LocalTime endTime;   // null means closed day

    @Column(nullable = false)
    private boolean active = true;
}