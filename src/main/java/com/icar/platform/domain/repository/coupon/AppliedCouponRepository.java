package com.icar.platform.domain.repository.coupon;

import com.icar.platform.domain.model.coupon.AppliedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

}