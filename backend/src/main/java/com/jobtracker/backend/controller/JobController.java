package com.jobtracker.backend.controller;

import com.jobtracker.backend.model.JobEntity;
import com.jobtracker.backend.model.RoundEntity;
import com.jobtracker.backend.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobEntity> createJob(@RequestBody JobEntity job, Authentication authentication) {
        log.info("[DEBUG] Creating job for user: {} with job: {}", authentication.getName(), job);
        JobEntity savedJob = jobService.createJob(job, authentication);
        log.info("[DEBUG] Saved job: {}", savedJob);
        return ResponseEntity.ok(savedJob);
    }

    @PostMapping("/{jobId}/rounds")
    public ResponseEntity<?> addRoundToJob(@PathVariable Long jobId, @RequestBody RoundEntity roundRequest) {
        jobService.addRoundToJob(jobId, roundRequest);
        return ResponseEntity.ok("Round added successfully");
    }

    @GetMapping
    public List<JobEntity> getUserJobs(Authentication authentication) {
        log.info("[DEBUG] Fetching jobs for user: {}", authentication.getName());
        List<JobEntity> jobs = jobService.getUserJobs(authentication);
        log.info("[DEBUG] Jobs found: {}", jobs.size());
        return jobs;
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobEntity> getJob(@PathVariable Long jobId, Authentication authentication) {
        JobEntity job = jobService.getJob(jobId, authentication);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{jobId}")
    public JobEntity updateJob(@PathVariable Long jobId, @RequestBody JobEntity job, Authentication authentication) {
        return jobService.updateJob(jobId, job, authentication);
    }

    @DeleteMapping("/{jobId}")
    public void deleteJob(@PathVariable Long jobId, Authentication authentication) {
        log.info("Deleting job with ID: {} for user: {}", jobId, authentication.getName());
        jobService.deleteJob(jobId, authentication);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getJobStats(Authentication authentication) {
        Map<String, Object> stats = jobService.getJobStats(authentication);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<JobEntity>> getRecentJobs(Authentication authentication) {
        List<JobEntity> recentJobs = jobService.getRecentJobs(authentication);
        return ResponseEntity.ok(recentJobs);
    }
}
