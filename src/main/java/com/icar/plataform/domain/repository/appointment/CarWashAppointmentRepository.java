package com.icar.plataform.domain.repository.appointment;

import com.icar.plataform.domain.enums.AppointmentStatus;
import com.icar.plataform.domain.model.appointment.CarWashAppointment;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @Query("SELECT a FROM CarWashAppointment a WHERE a.id = :appointmentId AND a.customer.id = :customerId")
    Optional<CarWashAppointment> findByIdAndCustomerId(
            @Param("appointmentId") UUID appointmentId,
            @Param("customerId") UUID customerId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM CarWashAppointment a " +
            "WHERE a.profile.carWashRegistration.id = :carWashId " +
            "AND a.dateTime BETWEEN :start AND :end " +
            "AND a.id <> :excludeId " +
            "AND a.status <> 'CANCELED'")
    boolean existsByCarWashIdAndDateTimeBetweenAndIdNot(
            @Param("carWashId") UUID carWashId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("excludeId") UUID excludeId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM CarWashAppointment a " +
            "WHERE a.profile.carWashRegistration.id = :carWashId " +
            "AND a.dateTime BETWEEN :start AND :end " +
            "AND a.status <> 'CANCELED'")
    boolean existsByCarWashIdAndDateTimeBetween(
            @Param("carWashId") UUID carWashId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @EntityGraph(attributePaths = {"address", "customer", "profile", "offering"})
    @Query("SELECT a FROM CarWashAppointment a " +
            "WHERE a.profile.carWashRegistration.id = :carWashId " +
            "AND a.status = 'CONFIRMED' " +
            "ORDER BY a.dateTime ASC")
    List<CarWashAppointment> findUpcomingConfirmedAppointmentsForCarWash(
            @Param("carWashId") UUID carWashId,
            @Param("now") LocalDateTime now);

    @Query("SELECT a FROM CarWashAppointment a JOIN FETCH a.address " +
            "WHERE a.customer.id = :customerId " +
            "AND a.status = 'CONFIRMED' " +
            "ORDER BY a.dateTime ASC")
    List<CarWashAppointment> findUpcomingConfirmedAppointmentsForCustomer(
            @Param("customerId") UUID customerId,
            @Param("now") LocalDateTime now);

    @Query("SELECT a FROM CarWashAppointment a JOIN FETCH a.address " +
            "WHERE a.customer.id = :customerId " +
            "AND (a.status = 'COMPLETED' OR a.status = 'CANCELED') " +
            "ORDER BY a.dateTime DESC")
    List<CarWashAppointment> getCompletedOrCanceledAppointmentsForCustomer(@Param("customerId") UUID customerId);


    @Query("SELECT a FROM CarWashAppointment a JOIN FETCH a.address " +
            "WHERE a.profile.carWashRegistration.id = :carWashId " +
            "AND a.status = :status " +
            "ORDER BY a.dateTime DESC")
    List<CarWashAppointment> findByCarWashIdAndStatusOrderByDateTimeDesc(
            @Param("carWashId") UUID carWashId,
            @Param("status") AppointmentStatus status);

    @Query("SELECT a FROM CarWashAppointment a " +
            "JOIN FETCH a.address " +
            "JOIN FETCH a.customer " +
            "JOIN FETCH a.profile " +
            "JOIN FETCH a.offering " +
            "WHERE a.profile.carWashRegistration.id = :carWashId " +
            "AND a.status = 'COMPLETED' " +
            "ORDER BY a.dateTime DESC")
    List<CarWashAppointment> findCompletedAppointmentsForCarWash(@Param("carWashId") UUID carWashId);

    @Query("SELECT a FROM CarWashAppointment a " +
            "JOIN FETCH a.address " +
            "JOIN FETCH a.customer " +
            "JOIN FETCH a.profile " +
            "JOIN FETCH a.offering " +
            "WHERE a.profile.carWashRegistration.id = :carWashId " +
            "AND a.status = 'CANCELED' " +
            "ORDER BY a.dateTime DESC")
    List<CarWashAppointment> findCanceledAppointmentsForCarWash(@Param("carWashId") UUID carWashId);

}
