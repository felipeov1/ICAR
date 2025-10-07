package com.icar.platform.application.service.notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icar.platform.domain.model.notification.PushSubscription;
import lombok.SneakyThrows;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.security.Security;
import java.util.Map;

@Service
public class WebPushService {

    @Value("${vapid.public.key}")
    private String publicKey;
    @Value("${vapid.private.key}")
    private String privateKey;
    @Value("${vapid.subject}")
    private String subject;

    private PushService pushService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void init() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        pushService = new PushService(publicKey, privateKey, subject);
    }

    @SneakyThrows
    public void sendNotification(PushSubscription sub, String title, String body) {
        Map<String, String> payload = Map.of("title", title, "body", body);
        String payloadJson = objectMapper.writeValueAsString(payload);

        Notification notification = new Notification(
                sub.getEndpoint(),
                sub.getP256dh(),
                sub.getAuth(),
                payloadJson
        );
        pushService.send(notification);
    }
}