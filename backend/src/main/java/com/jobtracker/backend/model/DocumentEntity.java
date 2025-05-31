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
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;      // Resume, Cover Letter, Certificate
    private String filePath;
    private String fileType; // PDF, DOC, DOCX
    private LocalDateTime uploadDate;
    private String version;
    private boolean isTemplate;
    private String description;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-documents")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    @JsonBackReference(value = "job-documents")
    private JobEntity job;
} 