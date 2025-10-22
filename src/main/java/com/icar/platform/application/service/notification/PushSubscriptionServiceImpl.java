package com.icar.platform.application.service.notification;

import com.icar.platform.api.dto.request.notification.PushSubscriptionRequest;
import com.icar.platform.domain.enums.SubscriptionType;
import com.icar.platform.domain.model.carwash.profile.CarWashProfile;
import com.icar.platform.domain.model.notification.PushSubscription;
import com.icar.platform.domain.repository.carwash.profile.CarWashProfileRepository;
import com.icar.platform.domain.repository.notification.PushSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PushSubscriptionServiceImpl implements PushSubscriptionService {

    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final CarWashProfileRepository carWashProfileRepository;

    @Override
    @Transactional
    public void subscribeCarWash(UUID profileId, PushSubscriptionRequest request) {
        CarWashProfile profile = carWashProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        PushSubscription subscription = new PushSubscription();
        subscription.setCarWashProfile(profile);
        subscription.setSubscriptionType(SubscriptionType.CARWASH);
        subscription.setEndpoint(request.getEndpoint());
        subscription.setP256dh(request.getKeys().getP256dh());
        subscription.setAuth(request.getKeys().getAuth());

        pushSubscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void subscribeAdmin(PushSubscriptionRequest request) {
        PushSubscription subscription = new PushSubscription();
        subscription.setCarWashProfile(null);
        subscription.setSubscriptionType(SubscriptionType.ADMIN);
        subscription.setEndpoint(request.getEndpoint());
        subscription.setP256dh(request.getKeys().getP256dh());
        subscription.setAuth(request.getKeys().getAuth());

        pushSubscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void unsubscribe(String endpoint) {
        pushSubscriptionRepository.deleteByEndpoint(endpoint);
    }
}