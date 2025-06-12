package com.icar.plataform.domain.repository.payment.coupon;

import com.icar.plataform.domain.model.payment.coupon.AppliedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppliedCouponRepository extends JpaRepository<AppliedCoupon, UUID> {

    @Query("SELECT ac FROM AppliedCoupon ac WHERE ac.appointment.id = :appointmentId")
    List<AppliedCoupon> findByAppointmentId(@Param("appointmentId") UUID appointmentId);

    @Query("SELECT ac FROM AppliedCoupon ac WHERE ac.appointment.id = :appointmentId AND ac.coupon.id = :couponId")
    Optional<AppliedCoupon> findByAppointmentAndCoupon(
            @Param("appointmentId") UUID appointmentId,
            @Param("couponId") UUID couponId);

    @Query("SELECT COUNT(ac) FROM AppliedCoupon ac WHERE ac.coupon.id = :couponId")
    int countByCouponId(@Param("couponId") UUID couponId);

    @Query("SELECT COUNT(ac) FROM AppliedCoupon ac WHERE ac.coupon.id = :couponId AND ac.customer.id = :customerId")
    int countByCouponAndCustomer(
            @Param("couponId") UUID couponId,
            @Param("customerId") UUID customerId);

    @Modifying
    @Query("DELETE FROM AppliedCoupon ac WHERE ac.appointment.id = :appointmentId")
    void deleteAllByAppointmentId(@Param("appointmentId") UUID appointmentId);

    @Query("SELECT ac FROM AppliedCoupon ac WHERE ac.customer.id = :customerId ORDER BY ac.appliedAt DESC")
    List<AppliedCoupon> findHistoryByCustomer(@Param("customerId") UUID customerId);

    @Query("SELECT CASE WHEN COUNT(ac) > 0 THEN true ELSE false END " +
            "FROM AppliedCoupon ac WHERE ac.appointment.id = :appointmentId AND ac.coupon.id = :couponId")
    boolean existsByAppointmentIdAndCouponId(
            @Param("appointmentId") UUID appointmentId,
            @Param("couponId") UUID couponId);
}