package com.jobtracker.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String priority; // High, Medium, Low
    private String status; // Pending, Completed, Cancelled
    private String type; // Generic, Application-specific, Interview-related
    private LocalDateTime reminderTime;
    private boolean isReminderSet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-tasks")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    @JsonBackReference(value = "job-tasks")
    private JobEntity job;
} 