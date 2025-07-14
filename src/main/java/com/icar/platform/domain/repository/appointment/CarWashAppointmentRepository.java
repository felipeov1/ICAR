package com.icar.platform.domain.repository.appointment;

import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarWashAppointmentRepository extends JpaRepository<CarWashAppointment, UUID> {

    String APPOINTMENT_FETCH_GRAPH = "SELECT DISTINCT a FROM CarWashAppointment a " +
            "JOIN FETCH a.profile p " +
            "JOIN FETCH a.address " +
            "JOIN FETCH p.carWashRegistration " +
            "JOIN FETCH a.offering o " +
            "LEFT JOIN FETCH o.vehiclePrices " +
            "LEFT JOIN FETCH o.vehicleEstimatedTimes " +
            "LEFT JOIN FETCH a.selectedExtraServices es " +
            "LEFT JOIN FETCH es.vehiclePrices " +
            "LEFT JOIN FETCH es.vehicleEstimatedTimes ";

    @Query(APPOINTMENT_FETCH_GRAPH +
            "WHERE a.customer.id = :customerId AND a.status = 'CONFIRMED' AND a.dateTime >= :now " +
            "ORDER BY a.dateTime ASC")
    List<CarWashAppointment> findUpcomingByCustomerId(@Param("customerId") UUID customerId, @Param("now") ZonedDateTime now);

    @Query(APPOINTMENT_FETCH_GRAPH +
            "WHERE a.customer.id = :customerId AND a.status IN ('COMPLETED', 'CANCELED') " +
            "ORDER BY a.dateTime DESC")
    List<CarWashAppointment> findHistoryByCustomerId(@Param("customerId") UUID customerId);

    @Query("SELECT a FROM CarWashAppointment a WHERE a.profile.carWashRegistration.id = :carWashId AND a.status = 'CONFIRMED' AND a.dateTime >= :now ORDER BY a.dateTime ASC")
    List<CarWashAppointment> findUpcomingByCarWashId(@Param("carWashId") UUID carWashId, @Param("now") ZonedDateTime now);

    @Query("SELECT a FROM CarWashAppointment a WHERE a.profile.carWashRegistration.id = :carWashId AND a.status = :status ORDER BY a.dateTime DESC")
    List<CarWashAppointment> findByCarWashIdAndStatus(@Param("carWashId") UUID carWashId, @Param("status") AppointmentStatus status);

    @Query("SELECT a FROM CarWashAppointment a WHERE a.profile.id = :profileId AND a.dateTime >= :start AND a.dateTime < :end AND a.status <> 'CANCELED'")
    List<CarWashAppointment> findBookedSlotsByProfileIdAndDateRange(
            @Param("profileId") UUID profileId,
            @Param("start") ZonedDateTime start,
            @Param("end") ZonedDateTime end
    );

    @Query("SELECT a FROM CarWashAppointment a WHERE a.status = 'CONFIRMED' AND a.dateTime BETWEEN :lookbackTime AND :now")
    List<CarWashAppointment> findCompletableAppointments(@Param("lookbackTime") ZonedDateTime lookbackTime, @Param("now") ZonedDateTime now);

    Optional<CarWashAppointment> findByIdAndCustomerId(UUID appointmentId, UUID customerId);

    @Query(value = "SELECT * FROM car_wash_appointment a WHERE a.profile_id = :profileId " +
            "AND a.status <> :#{#excludedStatus.name()} " +
            "AND a.date_time < :endTime " +
            "AND (a.date_time + (a.total_duration_minutes * INTERVAL '1 minute')) > :startTime",
            nativeQuery = true)
    List<CarWashAppointment> findConflictingAppointments(
            @Param("profileId") UUID profileId,
            @Param("excludedStatus") AppointmentStatus excludedStatus,
            @Param("startTime") ZonedDateTime startTime,
            @Param("endTime") ZonedDateTime endTime
    );

}