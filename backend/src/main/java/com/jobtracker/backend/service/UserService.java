package com.jobtracker.backend.service;

import com.jobtracker.backend.model.*;
import com.jobtracker.backend.repository.UserRepository;
import com.jobtracker.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements BaseService<UserEntity, Long> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserEntity save(UserEntity entity) {
        // Encode password before saving
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userRepository.save(entity);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity update(Long id, UserEntity entity) {
        UserEntity existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Update fields
        existingUser.setEmail(entity.getEmail());
        existingUser.setName(entity.getName());
        existingUser.setUniversity(entity.getUniversity());
        existingUser.setMajor(entity.getMajor());
        existingUser.setGraduationDate(entity.getGraduationDate());
        existingUser.setPortfolioUrl(entity.getPortfolioUrl());
        existingUser.setLinkedinUrl(entity.getLinkedinUrl());
        existingUser.setGithubUrl(entity.getGithubUrl());
        existingUser.setEmailNotificationsEnabled(entity.isEmailNotificationsEnabled());
        existingUser.setPushNotificationsEnabled(entity.isPushNotificationsEnabled());
        existingUser.setTheme(entity.getTheme());
        existingUser.setTimezone(entity.getTimezone());

        // Only update password if it's provided
        if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(entity.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void updateLastLogin(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastLoginDate(LocalDate.now());
        userRepository.save(user);
    }

    public void updateNotificationSettings(Long userId, boolean emailEnabled, boolean pushEnabled) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmailNotificationsEnabled(emailEnabled);
        user.setPushNotificationsEnabled(pushEnabled);
        userRepository.save(user);
    }

    public void updateTheme(Long userId, String theme) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setTheme(theme);
        userRepository.save(user);
    }

    public void updateTimezone(Long userId, String timezone) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setTimezone(timezone);
        userRepository.save(user);
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setUniversity(request.getUniversity());
        user.setMajor(request.getMajor());
        user.setGraduationDate(request.getGraduationDate());
        user.setEmailNotificationsEnabled(true);
        user.setPushNotificationsEnabled(true);
        user.setTheme("light");
        user.setTimezone("UTC");
        user.setActive(true);

        userRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserEntity user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getEmail());
        return new AuthenticationResponse(token);
    }
}
