package com.icar.platform.domain.repository.customer;

import com.icar.platform.domain.model.notification.DryWashSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DryWashSubscriptionRepository extends JpaRepository<DryWashSubscription, UUID> {
    boolean existsByCustomerId(UUID customerId);
}