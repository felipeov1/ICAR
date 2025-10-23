package com.icar.platform.application.service.notification;

import com.icar.platform.api.dto.response.notification.NotificationResponse;
import com.icar.platform.api.mapper.notification.NotificationMapper;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.enums.CreationChannel; // IMPORTANTE
import com.icar.platform.domain.enums.SubscriptionType; // IMPORTANTE
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.notification.Notification;
import com.icar.platform.domain.model.notification.PushSubscription;
import com.icar.platform.domain.repository.notification.NotificationRepository;
import com.icar.platform.domain.repository.notification.PushSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // IMPORTANTE
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async; // IMPORTANTE
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final WebPushService webPushService;
    private final NotificationMapper notificationMapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM 'às' HH:mm");

    @Async
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

        if (appointment.getCreationChannel() == CreationChannel.MARKETPLACE) {
            String adminTitle = "Admin: Novo Agendamento";
            String adminBody = "Novo agendamento em \"" + appointment.getProfile().getName() + "\" para " + appointment.getDateTime().format(FORMATTER);
            notifyAdmins(adminTitle, adminBody);
        }
    }

    @Async
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
            pushTitle = "Agendamento Cancelado \uD83D\uDEAB";
        }
        notification.setCreatedAt(Instant.now());
        notificationRepository.save(notification);

        String pushBody = notification.getText() + " " + notification.getAppointmentTime();
        sendPushNotificationToProfile(appointment.getProfile().getId(), pushTitle, pushBody);

        if (appointment.getCreationChannel() == CreationChannel.MARKETPLACE) {
            String adminTitle = (appointment.getStatus() == AppointmentStatus.REFUND_PENDING) ? "Admin: Reembolso Pendente" : "Admin: Cancelamento";
            String adminBody = "Agendamento em \"" + appointment.getProfile().getName() + "\" (" + appointment.getDateTime().format(FORMATTER) + ") foi cancelado/estornado.";
            notifyAdmins(adminTitle, adminBody);
        }
    }

    @Async
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

        String title = "Agendamento Alterado \uD83D\uDCC5";
        String body = notification.getText() + " " + notification.getAppointmentTime();
        sendPushNotificationToProfile(appointment.getProfile().getId(), title, body);

        if (appointment.getCreationChannel() == CreationChannel.MARKETPLACE) {
            String adminTitle = "Admin: Reagendamento";
            String adminBody = "Agendamento em \"" + appointment.getProfile().getName() + "\" foi reagendado para " + appointment.getDateTime().format(FORMATTER);
            notifyAdmins(adminTitle, adminBody);
        }
    }

    private void sendPushNotificationToProfile(UUID profileId, String title, String body) {
        List<PushSubscription> subscriptions = pushSubscriptionRepository.findByCarWashProfile_Id(profileId);

        if (subscriptions.isEmpty()) {
            log.info("Lava-rápido {} não possui inscrições push ativas.", profileId);
            return;
        }

        log.info("Enviando push '{}' para {} inscrições do perfil {}", title, subscriptions.size(), profileId);
        for (PushSubscription sub : subscriptions) {
            try {
                webPushService.sendNotification(sub, title, body);
            } catch (Exception e) {
                log.warn("Erro ao enviar push para endpoint: {}. Causa: {}", sub.getEndpoint(), e.getMessage());
            }
        }
    }

    private void notifyAdmins(String title, String body) {
        List<PushSubscription> adminSubs;
        try {
            adminSubs = pushSubscriptionRepository.findBySubscriptionType(SubscriptionType.ADMIN);
        } catch (Exception e) {
            log.error("Erro CRÍTICO ao buscar inscrições de ADMIN: {}", e.getMessage());
            return;
        }

        if (adminSubs.isEmpty()) {
            log.info("Nenhuma inscrição push de admin encontrada.");
            return;
        }

        log.info("Enviando notificação de admin '{}' para {} inscrições", title, adminSubs.size());
        for (PushSubscription sub : adminSubs) {
            try {
                webPushService.sendNotification(sub, title, body);
            } catch (Exception e) {
                log.warn("Falha ao enviar notificação para admin {}: {}", sub.getEndpoint(), e.getMessage());
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