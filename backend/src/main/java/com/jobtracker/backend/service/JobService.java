package com.jobtracker.backend.service;

import com.jobtracker.backend.model.*;
import com.jobtracker.backend.repository.JobRepository;
import com.jobtracker.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Transactional
    public JobEntity createJob(JobEntity job, Authentication authentication) {
        String email = authentication.getName();
        log.info("[DEBUG] JobService.createJob called for user: {} with job: {}", email, job);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        job.setUser(user);

        if (job.getDateApplied() == null) {
            job.setDateApplied(LocalDate.now());
        }

        if (job.getProgress() == 0) {
            job.setProgress(0);
        }

        if (job.getRounds() == null) {
            job.setRounds(new ArrayList<>());
        }

        JobEntity saved = null;
        try {
            saved = jobRepository.save(job);
            log.info("[DEBUG] JobService.createJob saved: {}", saved);
        } catch (Exception e) {
            log.error("[ERROR] Exception while saving job: {}", e.getMessage(), e);
            throw e;
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public List<JobEntity> getUserJobs(Authentication authentication) {
        String email = authentication.getName();
        log.info("[DEBUG] JobService.getUserJobs called for user: {}", email);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<JobEntity> jobs = jobRepository.findByUser(user);
        log.info("[DEBUG] JobService.getUserJobs found jobs: {}", jobs.size());
        return jobs;
    }

    @Transactional
    public void deleteJob(Long jobId, Authentication authentication) {
        String email = authentication.getName();
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    
        if (!job.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to delete this job");
        }
    
        log.info("Deleting job {} for user: {}", jobId, email);
        jobRepository.delete(job);
    }

    @Transactional
    public JobEntity updateJob(Long jobId, JobEntity updatedJob, Authentication authentication) {
        String email = authentication.getName();
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        if (!job.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to update this job");
        }
        
        job.setCompany(updatedJob.getCompany());
        job.setPosition(updatedJob.getPosition());
        job.setStatus(updatedJob.getStatus());
        job.setDateApplied(updatedJob.getDateApplied());
        job.setLocation(updatedJob.getLocation());
        job.setSource(updatedJob.getSource());
        job.setNotes(updatedJob.getNotes());
        job.setProgress(updatedJob.getProgress());
        job.setRounds(updatedJob.getRounds());
        return jobRepository.save(job);
    }

    @Transactional
    public void addRoundToJob(Long jobId, RoundEntity roundRequest) {
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        roundRequest.setJob(job);
        job.getRounds().add(roundRequest);
        jobRepository.save(job);
        log.info("Added new round to job {}", jobId);
    }

    @Transactional(readOnly = true)
    public JobEntity getJob(Long jobId, Authentication authentication) {
        String email = authentication.getName();
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        if (!job.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to access this job");
        }
        
        return job;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getJobStats(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<JobEntity> jobs = jobRepository.findByUser(user);
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalJobs", jobs.size());
        stats.put("activeJobs", jobs.stream().filter(job -> "Applied".equals(job.getStatus())).count());
        stats.put("interviewJobs", jobs.stream().filter(job -> "Interview".equals(job.getStatus())).count());
        stats.put("offeredJobs", jobs.stream().filter(job -> "Offered".equals(job.getStatus())).count());
        stats.put("rejectedJobs", jobs.stream().filter(job -> "Rejected".equals(job.getStatus())).count());
        
        return stats;
    }

    @Transactional(readOnly = true)
    public List<JobEntity> getRecentJobs(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return jobRepository.findTop5ByUserOrderByDateAppliedDesc(user);
    }
}
