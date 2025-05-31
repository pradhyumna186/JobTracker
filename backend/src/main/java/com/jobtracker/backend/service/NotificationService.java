package com.jobtracker.backend.service;

import com.jobtracker.backend.model.NotificationEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService extends BaseService<NotificationEntity, Long> {
    List<NotificationEntity> findByUserId(Long userId);
    List<NotificationEntity> findUnreadByUserId(Long userId);
    List<NotificationEntity> findByUserIdAndType(Long userId, String type);
    List<NotificationEntity> findRecentByUserId(Long userId, LocalDateTime date);
    List<NotificationEntity> findByJobId(Long jobId);
    long countUnreadByUserId(Long userId);
    NotificationEntity markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
} 