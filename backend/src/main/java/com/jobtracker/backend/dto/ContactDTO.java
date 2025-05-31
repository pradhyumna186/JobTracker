package com.jobtracker.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContactDTO {
    private Long id;
    private String name;
    private String company;
    private String role;
    private String email;
    private String linkedinUrl;
    private String relationshipType;
    private String notes;
    private LocalDateTime lastContactDate;
    private String lastContactNotes;
    private Long userId;
} 