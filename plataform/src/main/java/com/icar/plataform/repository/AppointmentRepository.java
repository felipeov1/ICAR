package com.icar.plataform.repository;

import com.icar.plataform.domain.model.Appointment;
import com.icar.plataform.domain.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByCustomerId(UUID customerId);
    List<Appointment> findByCarWashId(UUID carWashId);
    List<Appointment> findByCarWashIdAndDateTimeBetween(UUID carWashId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByCarWashIdAndStatus(UUID carWashId, AppointmentStatus status);
    boolean existsByCarWashIdAndDateTimeBetween(UUID carWashId, LocalDateTime start, LocalDateTime end);
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Appointment a " +
            "WHERE a.customer.id = :customerId " +
            "AND a.carWash.id = :carWashId " +
            "AND a.status = :status")
    boolean existsByCustomerIdAndCarWashIdAndStatus(
            @Param("customerId") UUID customerId,
            @Param("carWashId") UUID carWashId,
            @Param("status") AppointmentStatus status
    );
}
