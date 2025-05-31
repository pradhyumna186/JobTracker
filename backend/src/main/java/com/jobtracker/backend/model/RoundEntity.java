package com.jobtracker.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roundName;  // Phone Screen / Technical etc.

    private String status;     // Scheduled / Completed

    private String notes;      // Optional notes

    private LocalDateTime scheduledDateTime; // When round scheduled

    @ManyToOne
    @JoinColumn(name = "job_id")
    @JsonBackReference
    private JobEntity job;

}
