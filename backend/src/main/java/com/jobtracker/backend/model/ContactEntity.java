package com.jobtracker.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String company;
    private String role;
    private String email;
    private String linkedinUrl;
    private String relationshipType; // Recruiter, Alumni, Mentor, Peer
    private String notes;
    private LocalDateTime lastContactDate;
    private String lastContactNotes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-contacts")
    private UserEntity user;

    @ManyToMany
    @JoinTable(
        name = "contact_job",
        joinColumns = @JoinColumn(name = "contact_id"),
        inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    @JsonManagedReference(value = "contact-jobs")
    private List<JobEntity> associatedJobs = new ArrayList<>();
} 