package com.jobtracker.backend.service.impl;

import com.jobtracker.backend.model.TaskEntity;
import com.jobtracker.backend.repository.TaskRepository;
import com.jobtracker.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TaskEntity save(TaskEntity entity) {
        return taskRepository.save(entity);
    }

    @Override
    public List<TaskEntity> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<TaskEntity> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskEntity update(Long id, TaskEntity entity) {
        if (taskRepository.existsById(id)) {
            entity.setId(id);
            return taskRepository.save(entity);
        }
        throw new RuntimeException("Task not found with id: " + id);
    }

    @Override
    public List<TaskEntity> findByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public List<TaskEntity> findByUserIdAndStatus(Long userId, String status) {
        return taskRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<TaskEntity> findByUserIdAndPriority(Long userId, String priority) {
        return taskRepository.findByUserIdAndPriority(userId, priority);
    }

    @Override
    public List<TaskEntity> findOverdueTasks(Long userId, LocalDateTime date) {
        return taskRepository.findByUserIdAndDueDateBeforeAndStatusNot(userId, date, "Completed");
    }

    @Override
    public List<TaskEntity> findByJobId(Long jobId) {
        return taskRepository.findByJobId(jobId);
    }

    @Override
    public TaskEntity markAsComplete(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus("Completed");
        return taskRepository.save(task);
    }

    @Override
    public TaskEntity setReminder(Long taskId, LocalDateTime reminderTime) {
        TaskEntity task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setReminderTime(reminderTime);
        task.setReminderSet(true);
        return taskRepository.save(task);
    }
} 