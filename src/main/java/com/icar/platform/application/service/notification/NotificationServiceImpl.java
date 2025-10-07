package com.icar.platform.application.service.notification;

import com.icar.platform.api.dto.response.notification.NotificationResponse;
import com.icar.platform.api.mapper.notification.NotificationMapper;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.notification.Notification;
import com.icar.platform.domain.model.notification.PushSubscription;
import com.icar.platform.domain.repository.notification.NotificationRepository;
import com.icar.platform.domain.repository.notification.PushSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final WebPushService webPushService;
    private final NotificationMapper notificationMapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM 'às' HH:mm");

    @Override
    @Transactional
    public void createNotificationForNewAppointment(CarWashAppointment appointment) {
        Notification notification = new Notification();
        notification.setProfile(appointment.getProfile());
        notification.setType("NEW_APPOINTMENT");
        notification.setText("Novo agendamento recebido para");
        notification.setAppointmentTime(appointment.getDateTime().format(FORMATTER));
        notification.setCreatedAt(Instant.now());
        notificationRepository.save(notification);

        String title = "Novo Agendamento!";
        String body = notification.getText() + " " + notification.getAppointmentTime();
        sendPushNotificationToProfile(appointment.getProfile().getId(), title, body);
    }

    @Override
    @Transactional
    public void createNotificationForCancelledAppointment(CarWashAppointment appointment) {
        Notification notification = new Notification();
        notification.setProfile(appointment.getProfile());

        String pushTitle;
        if (appointment.getStatus() == AppointmentStatus.REFUND_PENDING) {
            notification.setType("REFUND_REQUIRED");
            notification.setText("Ação necessária: Cancelamento com reembolso para");
            notification.setAppointmentTime(appointment.getDateTime().format(FORMATTER) + ". Efetue o estorno.");
            pushTitle = "Reembolso Necessário";
        } else {
            notification.setType("CANCELLATION");
            notification.setText("O agendamento para");
            notification.setAppointmentTime(appointment.getDateTime().format(FORMATTER) + " foi cancelado.");
            pushTitle = "Agendamento Cancelado";
        }

        notification.setCreatedAt(Instant.now());
        notificationRepository.save(notification);

        String pushBody = notification.getText() + " " + notification.getAppointmentTime();
        sendPushNotificationToProfile(appointment.getProfile().getId(), pushTitle, pushBody);
    }

    @Override
    @Transactional
    public void createNotificationForEditedAppointment(CarWashAppointment appointment) {
        Notification notification = new Notification();
        notification.setProfile(appointment.getProfile());
        notification.setType("EDITION");
        notification.setText("O agendamento para");
        notification.setAppointmentTime(appointment.getDateTime().format(FORMATTER) + " foi alterado.");
        notification.setCreatedAt(Instant.now());
        notificationRepository.save(notification);

        String title = "Agendamento Alterado";
        String body = notification.getText() + " " + notification.getAppointmentTime();
        sendPushNotificationToProfile(appointment.getProfile().getId(), title, body);
    }

    private void sendPushNotificationToProfile(UUID profileId, String title, String body) {
        List<PushSubscription> subscriptions = pushSubscriptionRepository.findByCarWashProfileId(profileId);

        for (PushSubscription sub : subscriptions) {
            try {
                webPushService.sendNotification(sub, title, body);
            } catch (Exception e) {
                System.err.println("Erro ao enviar push para endpoint: " + sub.getEndpoint() + ". Causa: " + e.getMessage());
            }
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsForProfile(UUID profileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notificationPage = notificationRepository.findByProfileIdOrderByCreatedAtDesc(profileId, pageable);
        return notificationPage.map(notificationMapper::toResponse);
    }

    @Override
    @Transactional
    public void markAllAsRead(UUID profileId) {
        notificationRepository.markAllAsRead(profileId);
    }
}