package com.icar.plataform.domain.repository.payment.coupon;

import com.icar.plataform.domain.model.payment.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    Optional<Coupon> findByCode(String code);

    @Query("SELECT c FROM Coupon c WHERE " +
            "c.code = :code AND " +
            "c.validFrom <= :currentDate AND " +
            "c.validUntil >= :currentDate AND " +
            "(c.maxUses IS NULL OR c.currentUses < c.maxUses)")
    Optional<Coupon> findValidCouponByCode(
            @Param("code") String code,
            @Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT c FROM Coupon c WHERE c.profile.id = :profileId")
    List<Coupon> findByProfileId(@Param("profileId") UUID profileId);

    @Query("SELECT COUNT(ac) FROM AppliedCoupon ac WHERE " +
            "ac.coupon.id = :couponId AND " +
            "ac.customer.id = :customerId")
    int countUsesByCustomer(
            @Param("couponId") UUID couponId,
            @Param("customerId") UUID customerId);

    boolean existsByCode(String code);
}