package com.icar.platform.domain.repository.appointment;

import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarWashAppointmentRepository extends JpaRepository<CarWashAppointment, UUID> {

    String APPOINTMENT_FETCH_GRAPH = "SELECT DISTINCT a FROM CarWashAppointment a " +
            "JOIN FETCH a.profile p " +
            "JOIN FETCH a.address " +
            "JOIN FETCH p.carWashRegistration " +
            "LEFT JOIN FETCH a.selectedServices s " +
            "LEFT JOIN FETCH s.vehicleDetails ";

    String APPOINTMENT_FETCH_GRAPH_NATIVE_SELECT = "SELECT DISTINCT a.* FROM car_wash_appointment a ";

    @Query(value = APPOINTMENT_FETCH_GRAPH_NATIVE_SELECT +
            "WHERE a.customer_id = :customerId AND a.status = 'CONFIRMED' " +
            "ORDER BY a.date_time ASC",
            nativeQuery = true)
    List<CarWashAppointment> findUpcomingByCustomerId(@Param("customerId") UUID customerId);

    @Query(APPOINTMENT_FETCH_GRAPH +
            "WHERE a.customer.id = :customerId AND a.status IN ('COMPLETED', 'CANCELED', 'REFUND_PENDING') " +
            "ORDER BY a.updatedAt DESC NULLS LAST, a.createdAt DESC")
    List<CarWashAppointment> findHistoryByCustomerId(@Param("customerId") UUID customerId);

    @Query("SELECT a FROM CarWashAppointment a WHERE a.profile.id = :profileId AND a.dateTime >= :start AND a.dateTime < :end AND a.status <> 'CANCELED'")
    List<CarWashAppointment> findBookedSlotsByProfileIdAndDateRange(
            @Param("profileId") UUID profileId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    Optional<CarWashAppointment> findByIdAndCustomerId(UUID appointmentId, UUID customerId);

    @Query(value = "SELECT * FROM car_wash_appointment a WHERE a.profile_id = :profileId " +
            "AND a.status <> CAST(:excludedStatus AS text) " +
            "AND a.date_time < :endTime " +
            "AND (a.date_time + (a.total_duration_minutes * INTERVAL '1 minute')) > :startTime",
            nativeQuery = true)
    List<CarWashAppointment> findConflictingAppointments(
            @Param("profileId") UUID profileId,
            @Param("excludedStatus") String excludedStatus,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query(value = "SELECT * FROM car_wash_appointment a WHERE a.profile_id = :profileId " +
            "AND a.id <> :appointmentIdToIgnore " +
            "AND a.status <> CAST(:excludedStatus AS text) " +
            "AND a.date_time < :endTime " +
            "AND (a.date_time + (a.total_duration_minutes * INTERVAL '1 minute')) > :startTime",
            nativeQuery = true)
    List<CarWashAppointment> findConflictingAppointmentsExcludingId(
            @Param("profileId") UUID profileId,
            @Param("excludedStatus") String excludedStatus,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("appointmentIdToIgnore") UUID appointmentIdToIgnore);

    @Query("SELECT a FROM CarWashAppointment a WHERE a.profile.id = :profileId AND a.status = 'CONFIRMED' ORDER BY a.dateTime ASC")
    List<CarWashAppointment> findUpcomingByProfileId(@Param("profileId") UUID profileId);

    @Query("SELECT a FROM CarWashAppointment a WHERE a.profile.id = :profileId AND a.status = :status ORDER BY a.dateTime DESC")
    List<CarWashAppointment> findByProfileIdAndStatus(@Param("profileId") UUID profileId, @Param("status") AppointmentStatus status);

    @Query("SELECT a FROM CarWashAppointment a " +
            "WHERE a.profile.id = :profileId " +
            "AND a.status IN ('CONFIRMED', 'COMPLETED', 'REFUND_PENDING') " +
            "AND a.dateTime >= :startDate AND a.dateTime < :endDate")
    List<CarWashAppointment> findByProfileIdAndDateRange(
            @Param("profileId") UUID profileId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM CarWashAppointment a LEFT JOIN FETCH a.selectedServices WHERE a.profile.id = :profileId AND a.status = :status AND a.dateTime BETWEEN :start AND :end")
    List<CarWashAppointment> findAllByProfileIdAndStatusAndDateTimeBetween(
            @Param("profileId") UUID profileId,
            @Param("status") AppointmentStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT a FROM CarWashAppointment a " +
            "WHERE a.profile.id = :profileId " +
            "AND a.status != 'CANCELED' " +
            "AND a.dateTime >= :startDateTime " +
            "AND a.dateTime < :endDateTime")
    List<CarWashAppointment> findActiveByProfileIdAndDateTimeRange(
            @Param("profileId") UUID profileId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    Optional<CarWashAppointment> findByIdAndProfileId(UUID appointmentId, UUID profileId);

    long countByProfileIdAndStatus(UUID profileId, AppointmentStatus status);

}