package com.jobtracker.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Embeddable
public class InterviewRound {

    private String roundName; // Phone Screen, Technical Interview
    private String status;    // Scheduled, Completed, Passed, Failed
    private String notes;     // Notes about the round

    private LocalDateTime scheduledDateTime; // 🕒 New: Scheduled DateTime
    private String location; // 📍 New: Zoom/Google Meet/Office etc.
}
