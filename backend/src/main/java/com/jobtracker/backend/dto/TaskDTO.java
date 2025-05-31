package com.jobtracker.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String priority;
    private String status;
    private String type;
    private LocalDateTime reminderTime;
    private boolean isReminderSet;
    private Long userId;
    private Long jobId;
} 