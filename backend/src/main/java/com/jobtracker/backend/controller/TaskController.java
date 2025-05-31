package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.TaskDTO;
import com.jobtracker.backend.model.TaskEntity;
import com.jobtracker.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskEntity task = convertToEntity(taskDTO);
        TaskEntity savedTask = taskService.save(task);
        return ResponseEntity.ok(convertToDTO(savedTask));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskEntity> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return taskService.findById(id)
            .map(task -> ResponseEntity.ok(convertToDTO(task)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(@PathVariable Long userId) {
        List<TaskEntity> tasks = taskService.findByUserId(userId);
        return ResponseEntity.ok(tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<TaskDTO>> getTasksByUserIdAndStatus(
            @PathVariable Long userId, @PathVariable String status) {
        List<TaskEntity> tasks = taskService.findByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}/priority/{priority}")
    public ResponseEntity<List<TaskDTO>> getTasksByUserIdAndPriority(
            @PathVariable Long userId, @PathVariable String priority) {
        List<TaskEntity> tasks = taskService.findByUserIdAndPriority(userId, priority);
        return ResponseEntity.ok(tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}/overdue")
    public ResponseEntity<List<TaskDTO>> getOverdueTasks(@PathVariable Long userId) {
        List<TaskEntity> tasks = taskService.findOverdueTasks(userId, LocalDateTime.now());
        return ResponseEntity.ok(tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<TaskDTO>> getTasksByJobId(@PathVariable Long jobId) {
        List<TaskEntity> tasks = taskService.findByJobId(jobId);
        return ResponseEntity.ok(tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        TaskEntity task = convertToEntity(taskDTO);
        TaskEntity updatedTask = taskService.update(id, task);
        return ResponseEntity.ok(convertToDTO(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskDTO> markTaskAsComplete(@PathVariable Long id) {
        TaskEntity task = taskService.markAsComplete(id);
        return ResponseEntity.ok(convertToDTO(task));
    }

    @PutMapping("/{id}/reminder")
    public ResponseEntity<TaskDTO> setTaskReminder(
            @PathVariable Long id, @RequestBody LocalDateTime reminderTime) {
        TaskEntity task = taskService.setReminder(id, reminderTime);
        return ResponseEntity.ok(convertToDTO(task));
    }

    private TaskDTO convertToDTO(TaskEntity task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setType(task.getType());
        dto.setReminderTime(task.getReminderTime());
        dto.setReminderSet(task.isReminderSet());
        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
        }
        if (task.getJob() != null) {
            dto.setJobId(task.getJob().getId());
        }
        return dto;
    }

    private TaskEntity convertToEntity(TaskDTO dto) {
        TaskEntity task = new TaskEntity();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setType(dto.getType());
        task.setReminderTime(dto.getReminderTime());
        task.setReminderSet(dto.isReminderSet());
        return task;
    }
} 