package com.jobtracker.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private Long userId;
    private Long jobId;
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private String priority;
    private String actionUrl;
    private LocalDateTime createdAt;
} 