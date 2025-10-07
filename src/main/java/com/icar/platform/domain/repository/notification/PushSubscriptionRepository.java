package com.icar.platform.domain.repository.notification; // Seu pacote

// ADICIONE ESTES IMPORTS
import com.icar.platform.domain.model.notification.PushSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, UUID> {
    List<PushSubscription> findByCarWashProfileId(UUID profileId);
    void deleteByEndpoint(String endpoint);
}