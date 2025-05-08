package com.icar.plataform.domain.repository;

import com.icar.plataform.domain.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
    List<Promotion> findByCarWashId(UUID carWashId);
    List<Promotion> findByServiceId(UUID serviceId);
    List<Promotion> findByCarWashIdAndEndDateAfter(UUID carWashId, LocalDate date);

    @Query("SELECT p FROM Promotion p WHERE p.service.id = :serviceId " +
            "AND p.startDate <= CURRENT_DATE AND p.endDate >= CURRENT_DATE")
    Optional<Promotion> findActivePromotion(@Param("serviceId") UUID serviceId);

    Optional<Promotion> findByServiceIdAndStartDateBeforeAndEndDateAfter(
            UUID serviceId,
            LocalDateTime start,
            LocalDateTime end);
}
