package com.icar.platform.application.service.notification;

import com.icar.platform.api.dto.response.notification.NotificationResponse;
import com.icar.platform.api.mapper.notification.NotificationMapper;
import com.icar.platform.domain.enums.AppointmentStatus;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import com.icar.platform.domain.model.notification.Notification;
import com.icar.platform.domain.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
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
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void createNotificationForCancelledAppointment(CarWashAppointment appointment) {
        Notification notification = new Notification();
        notification.setProfile(appointment.getProfile());

        if (appointment.getStatus() == AppointmentStatus.REFUND_PENDING) {
            notification.setType("REFUND_REQUIRED");
            notification.setText("Ação necessária: Cancelamento com reembolso para");
            notification.setAppointmentTime(appointment.getDateTime().format(FORMATTER) + ". Efetue o estorno.");
        } else {
            notification.setType("CANCELLATION");
            notification.setText("O agendamento para");
            notification.setAppointmentTime(appointment.getDateTime().format(FORMATTER) + " foi cancelado.");
        }

        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void createNotificationForEditedAppointment(CarWashAppointment appointment) {
        Notification notification = new Notification();
        notification.setProfile(appointment.getProfile());
        notification.setType("EDITION");
        notification.setText("O agendamento para");
        notification.setAppointmentTime(appointment.getDateTime().format(FORMATTER) + " foi alterado.");
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
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