package com.icar.platform.domain.repository.notification;

import com.icar.platform.domain.model.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findByProfileIdOrderByCreatedAtDesc(UUID profileId, Pageable pageable);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.profile.id = :profileId AND n.read = false")
    void markAllAsRead(@Param("profileId") UUID profileId);
}