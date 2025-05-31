package com.jobtracker.backend.model;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
