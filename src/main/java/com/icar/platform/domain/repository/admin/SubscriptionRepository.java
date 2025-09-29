package com.icar.platform.domain.repository.admin;

import com.icar.platform.domain.enums.SubscriptionStatus;
import com.icar.platform.domain.model.admin.Subscription;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    @EntityGraph(attributePaths = {"plan"})
    List<Subscription> findAllByStatus(SubscriptionStatus status);

    long countByStatus(SubscriptionStatus status);

    @EntityGraph(attributePaths = {"plan"})
    List<Subscription> findByStatusAndStartDateAfter(SubscriptionStatus status, LocalDateTime startDate);

    @EntityGraph(attributePaths = {"plan"})
    @Query("SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' AND s.startDate <= :dateTime AND (s.endDate IS NULL OR s.endDate > :dateTime)")
    List<Subscription> findActiveSubscriptionsAt(@Param("dateTime") LocalDateTime dateTime);

    @EntityGraph(attributePaths = {"plan"})
    List<Subscription> findByStatusAndStartDateBetween(SubscriptionStatus status, LocalDateTime startDate, LocalDateTime endDate);
    List<Subscription> findByStatusAndStartDateBefore(SubscriptionStatus status, LocalDateTime endDate);
}