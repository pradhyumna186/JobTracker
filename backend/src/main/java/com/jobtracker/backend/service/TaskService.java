package com.jobtracker.backend.service;

import com.jobtracker.backend.model.TaskEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskService extends BaseService<TaskEntity, Long> {
    List<TaskEntity> findByUserId(Long userId);
    List<TaskEntity> findByUserIdAndStatus(Long userId, String status);
    List<TaskEntity> findByUserIdAndPriority(Long userId, String priority);
    List<TaskEntity> findOverdueTasks(Long userId, LocalDateTime date);
    List<TaskEntity> findByJobId(Long jobId);
    TaskEntity markAsComplete(Long taskId);
    TaskEntity setReminder(Long taskId, LocalDateTime reminderTime);
} 