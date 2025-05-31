package com.jobtracker.backend.controller;

import com.jobtracker.backend.model.JobEntity;
import com.jobtracker.backend.model.RoundEntity;
import com.jobtracker.backend.repository.JobRepository;
import com.jobtracker.backend.service.JobService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobRepository jobRepository;

    // ✅ THIS IS NEWLY ADDED
    @PostMapping
    public ResponseEntity<JobEntity> createJob(@RequestBody JobEntity job, Authentication authentication) {
        JobEntity savedJob = jobService.createJob(job, authentication);
        return ResponseEntity.ok(savedJob);
    }

    // ✅ This already exists and is correct
    @PostMapping("/{jobId}/rounds")
    public ResponseEntity<?> addRoundToJob(@PathVariable Long jobId, @RequestBody RoundEntity roundRequest) {
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        roundRequest.setJob(job); // connect round to this job
        job.getRounds().add(roundRequest);

        jobRepository.save(job);

        return ResponseEntity.ok("Round added successfully");
    }

    @GetMapping
    public List<JobEntity> getUserJobs(Authentication authentication) {
        return jobService.getUserJobs(authentication);
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
        System.out.println("Received request to delete job with ID: " + jobId);
        System.out.println("User trying to delete: " + authentication.getName());
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
