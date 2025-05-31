package com.jobtracker.backend.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String university;
    private String major;
    private LocalDate graduationDate;
}
