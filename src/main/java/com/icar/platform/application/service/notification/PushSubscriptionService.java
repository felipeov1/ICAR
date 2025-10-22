package com.icar.platform.application.service.notification;

import com.icar.platform.api.dto.request.notification.PushSubscriptionRequest;
import java.util.UUID;

public interface PushSubscriptionService {
    void subscribeCarWash(UUID profileId, PushSubscriptionRequest request);

    void subscribeAdmin(PushSubscriptionRequest request);

    void unsubscribe(String endpoint);
}