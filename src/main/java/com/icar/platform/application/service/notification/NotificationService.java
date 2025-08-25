package com.icar.platform.application.service.notification;

import com.icar.platform.api.dto.response.notification.NotificationResponse;
import com.icar.platform.domain.model.appointment.CarWashAppointment;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void createNotificationForNewAppointment(CarWashAppointment appointment);
    void createNotificationForCancelledAppointment(CarWashAppointment appointment);
    void createNotificationForEditedAppointment(CarWashAppointment appointment);
    Page<NotificationResponse> getNotificationsForProfile(UUID profileId, int page, int size);
    void markAllAsRead(UUID profileId);
}