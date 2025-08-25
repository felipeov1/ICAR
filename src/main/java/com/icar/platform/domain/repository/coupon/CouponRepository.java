package com.icar.platform.domain.repository.coupon;

import com.icar.platform.domain.model.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    @Query("SELECT c FROM Coupon c WHERE c.code = :code AND (c.profile.id = :profileId OR c.profile.id IS NULL)")
    Optional<Coupon> findValidByCodeAndProfile(@Param("code") String code, @Param("profileId") UUID profileId);

    boolean existsByCode(String code);

    List<Coupon> findByProfileId(UUID profileId);
}