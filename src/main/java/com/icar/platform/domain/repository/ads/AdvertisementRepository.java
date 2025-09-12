package com.icar.platform.domain.repository.ads;

import com.icar.platform.domain.model.advertisement.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, UUID> {

    @Query("SELECT a FROM Advertisement a WHERE a.isPlatformAd = false AND a.isActive = true AND a.expiresAt > :now")
    List<Advertisement> findActivePaidAds(@Param("now") LocalDateTime now);

    @Query("SELECT a FROM Advertisement a WHERE a.isPlatformAd = true AND a.isActive = true")
    List<Advertisement> findActivePlatformAds();

    List<Advertisement> findAllByIsDeletedFalse();

    @Query("SELECT a FROM Advertisement a WHERE a.isDeleted = false AND a.isActive = true AND (a.expiresAt IS NULL OR a.expiresAt > :now)")
    List<Advertisement> findActiveAdvertisements(@Param("now") LocalDateTime now);
}