package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByUserId(Long userId);
    List<TaskEntity> findByUserIdAndStatus(Long userId, String status);
    List<TaskEntity> findByUserIdAndPriority(Long userId, String priority);
    List<TaskEntity> findByUserIdAndDueDateBeforeAndStatusNot(Long userId, LocalDateTime date, String status);
    List<TaskEntity> findByJobId(Long jobId);
} 