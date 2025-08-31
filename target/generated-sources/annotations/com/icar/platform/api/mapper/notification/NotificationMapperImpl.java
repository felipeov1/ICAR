package com.icar.platform.api.mapper.notification;

import com.icar.platform.api.dto.response.notification.NotificationResponse;
import com.icar.platform.domain.model.notification.Notification;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-31T18:24:59-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationResponse toResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        boolean read = false;
        UUID id = null;
        String type = null;
        String text = null;
        String appointmentTime = null;
        LocalDateTime createdAt = null;

        read = notification.isRead();
        id = notification.getId();
        type = notification.getType();
        text = notification.getText();
        appointmentTime = notification.getAppointmentTime();
        createdAt = notification.getCreatedAt();

        NotificationResponse notificationResponse = new NotificationResponse( id, type, text, appointmentTime, read, createdAt );

        return notificationResponse;
    }
}
