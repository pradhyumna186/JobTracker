package com.jobtracker.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private String position;
    private String status;

    private String jobType;   // Internship / Full-Time
    private String field;     // SDE / Data Science / etc.
    private String role;      // Backend Developer etc.

    private Integer progress;  // 0–100%

    private LocalDate dateApplied;

    private String location;

    private String salaryRange;

    private String notes;

    private String jobPostUrl;

    // 🔥 New Features Added:
    private String applicationSource; // LinkedIn, Company Website, Referral, etc.

    private String applicationLink;   // URL to job post

    private LocalDateTime nextInterviewDate; // Next scheduled interview datetime

    private String priority; // High / Normal

    private String offerSalary; // Salary offered if offer received

    private LocalDate joiningDate; // Expected joining date

    private String rejectionReason; // Reason if rejected

    private Boolean archived; // Mark job as archived (default false)

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserEntity user;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RoundEntity> rounds = new ArrayList<>();

}
