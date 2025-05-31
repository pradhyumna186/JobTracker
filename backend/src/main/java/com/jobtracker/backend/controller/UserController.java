package com.jobtracker.backend.controller;

import com.jobtracker.backend.model.UserEntity;
import com.jobtracker.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserEntity> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserByEmail(authentication.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserEntity> updateProfile(
            @RequestBody UserEntity updatedUser,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateProfile(authentication.getName(), updatedUser));
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
            @RequestBody Map<String, String> passwords,
            Authentication authentication) {
        userService.updatePassword(
                authentication.getName(),
                passwords.get("currentPassword"),
                passwords.get("newPassword")
        );
        return ResponseEntity.ok().build();
    }
} 