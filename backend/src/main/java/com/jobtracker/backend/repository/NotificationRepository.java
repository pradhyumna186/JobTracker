package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUser_Id(Long userId);
    List<NotificationEntity> findByUser_IdAndIsRead(Long userId, boolean isRead);
    List<NotificationEntity> findByUser_IdAndType(Long userId, String type);
    List<NotificationEntity> findByUser_IdAndCreatedAtAfter(Long userId, LocalDateTime date);
    List<NotificationEntity> findByJob_Id(Long jobId);
    long countByUser_IdAndIsRead(Long userId, boolean isRead);
} 