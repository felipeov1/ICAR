package com.icar.plataform.domain.repository;

import com.icar.plataform.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByCarWashId(UUID carWashId);
    List<Review> findByCustomerId(UUID customerId);
    boolean existsByCustomerIdAndCarWashIdAndServiceId(UUID customerId, UUID carWashId, UUID serviceId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.carWashId = :carWashId")
    Double calculateAverageRatingByCarWashId(@Param("carWashId") UUID carWashId);
}
