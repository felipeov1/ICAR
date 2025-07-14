package com.icar.platform.domain.model.carwash.profile;

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
    private Integer minAdvanceNoticeMinutes;

    @Column(name = "min_edit_notice_minutes", nullable = false)
    private Integer minEditNoticeMinutes;

    @Column(name = "min_cancel_notice_minutes", nullable = false)
    private Integer minCancelNoticeMinutes;

    @Column(name = "max_advance_booking_days", nullable = false)
    private Integer maxAdvanceBookingDays;

    @Column(name = "gap_minutes", nullable = false)
    private Integer gapMinutes;

    @Column(name = "allow_overtime", nullable = false)
    private boolean allowOvertime;
}