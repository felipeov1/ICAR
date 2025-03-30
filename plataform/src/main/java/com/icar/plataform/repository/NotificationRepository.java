package com.icar.plataform.repository;

import com.icar.plataform.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdAndUserTypeOrderByCreatedAtDesc(UUID userId, String userType);
    List<Notification> findByUserIdAndUserTypeAndIsRead(UUID userId, String userType, Boolean isRead);
}
