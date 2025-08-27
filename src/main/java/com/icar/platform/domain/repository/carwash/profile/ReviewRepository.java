package com.icar.platform.domain.repository.carwash.profile;

import com.icar.platform.domain.model.carwash.profile.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // 1. Adicione este import

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByProfileIdOrderByCreatedAtDesc(UUID profileId);

    boolean existsByAppointmentId(UUID appointmentId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.profile.id = :profileId")
    Double calculateAverageRating(@Param("profileId") UUID profileId);

    long countByProfileId(UUID profileId);

    List<Review> findByProfileIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            UUID profileId,
            LocalDateTime start,
            LocalDateTime end
    );
}