package com.icar.platform.api.mapper.notification;

import com.icar.platform.api.dto.response.notification.NotificationResponse;
import com.icar.platform.domain.model.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(source = "read", target = "read")
    NotificationResponse toResponse(Notification notification);
}
