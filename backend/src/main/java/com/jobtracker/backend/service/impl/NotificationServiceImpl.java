package com.jobtracker.backend.service.impl;

import com.jobtracker.backend.model.NotificationEntity;
import com.jobtracker.backend.repository.NotificationRepository;
import com.jobtracker.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public NotificationEntity save(NotificationEntity entity) {
        return notificationRepository.save(entity);
    }

    @Override
    public List<NotificationEntity> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<NotificationEntity> findById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public NotificationEntity update(Long id, NotificationEntity entity) {
        if (notificationRepository.existsById(id)) {
            entity.setId(id);
            return notificationRepository.save(entity);
        }
        throw new RuntimeException("Notification not found with id: " + id);
    }

    @Override
    public List<NotificationEntity> findByUserId(Long userId) {
        return notificationRepository.findByUser_Id(userId);
    }

    @Override
    public List<NotificationEntity> findUnreadByUserId(Long userId) {
        return notificationRepository.findByUser_IdAndIsRead(userId, false);
    }

    @Override
    public List<NotificationEntity> findByUserIdAndType(Long userId, String type) {
        return notificationRepository.findByUser_IdAndType(userId, type);
    }

    @Override
    public List<NotificationEntity> findRecentByUserId(Long userId, LocalDateTime date) {
        return notificationRepository.findByUser_IdAndCreatedAtAfter(userId, date);
    }

    @Override
    public List<NotificationEntity> findByJobId(Long jobId) {
        return notificationRepository.findByJob_Id(jobId);
    }

    @Override
    public long countUnreadByUserId(Long userId) {
        return notificationRepository.countByUser_IdAndIsRead(userId, false);
    }

    @Override
    public NotificationEntity markAsRead(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<NotificationEntity> unreadNotifications = findUnreadByUserId(userId);
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
} 