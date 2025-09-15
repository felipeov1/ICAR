package com.icar.platform.domain.repository.admin;

import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.coupon.AppliedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppliedCouponRepository extends JpaRepository<AppliedCoupon, UUID> {
    List<AppliedCoupon> findByAppointmentId(UUID appointmentId);

    List<AppliedCoupon> findHistoryByCustomerId(UUID customerId);

    void deleteAllByAppointmentId(UUID appointmentId);

    int countByCouponId(UUID couponId);

    boolean existsByAppointmentIdAndCouponId(UUID appointmentId, UUID couponId);

    long countByCouponIdAndCustomerId(UUID couponId, UUID customerId);

    List<AppliedCoupon> findByAppliedAtBetween(ZonedDateTime start, ZonedDateTime end);

    @Query("SELECT COUNT(ac) FROM AppliedCoupon ac WHERE ac.coupon.id = :couponId AND ac.appointment.status = :status")
    int countByCouponIdAndAppointmentStatus(@Param("couponId") UUID couponId, @Param("status") AppointmentStatus status);

    @Query("SELECT ac FROM AppliedCoupon ac WHERE ac.appointment.profile.id = :partnerId AND ac.appliedAt BETWEEN :start AND :end")
    List<AppliedCoupon> findByAppliedAtBetweenAndPartnerId(@Param("start") ZonedDateTime start, @Param("end") ZonedDateTime end, @Param("partnerId") UUID partnerId);
    @Query("SELECT ac FROM AppliedCoupon ac WHERE ac.appointment.updatedAt BETWEEN :start AND :end")
    List<AppliedCoupon> findByAppointmentUpdatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT ac FROM AppliedCoupon ac WHERE ac.appointment.profile.id = :partnerId AND ac.appointment.updatedAt BETWEEN :start AND :end")
    List<AppliedCoupon> findByAppointmentUpdatedAtBetweenAndPartnerId(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("partnerId") UUID partnerId);
}
