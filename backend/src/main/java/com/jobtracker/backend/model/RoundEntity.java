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
public class RoundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;      // Phone, Technical, On-site
    private String status;    // Scheduled, Completed, Cancelled
    private LocalDateTime date;
    private String notes;     // Optional notes

    @ManyToOne
    @JoinColumn(name = "job_id")
    @JsonBackReference(value = "job-rounds")
    private JobEntity job;
}
