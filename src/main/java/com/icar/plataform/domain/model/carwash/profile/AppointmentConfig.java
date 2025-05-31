package com.icar.plataform.domain.model.carwash.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "car_wash_profile_appointment_config")
public class AppointmentConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    private CarWashProfile profile;

    @Column(name = "min_advance_notice_minutes", nullable = false)
    private Integer minAdvanceNoticeMinutes; // Minutos mínimos para agendar

    @Column(name = "min_edit_notice_minutes", nullable = false)
    private Integer minEditNoticeMinutes; // Minutos mínimos para editar

    @Column(name = "min_cancel_notice_minutes", nullable = false)
    private Integer minCancelNoticeMinutes; // Minutos mínimos para cancelar
}