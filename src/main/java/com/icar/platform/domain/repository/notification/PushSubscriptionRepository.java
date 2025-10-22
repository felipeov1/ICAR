package com.icar.platform.domain.repository.notification;

import com.icar.platform.domain.enums.SubscriptionType;
import com.icar.platform.domain.model.notification.PushSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, UUID> {
    List<PushSubscription> findByCarWashProfileId(UUID profileId);
    void deleteByEndpoint(String endpoint);
    List<PushSubscription> findByCarWashProfile_Id(UUID profileId);
    List<PushSubscription> findBySubscriptionType(SubscriptionType subscriptionType);
}