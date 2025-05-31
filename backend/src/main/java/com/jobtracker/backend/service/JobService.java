package com.jobtracker.backend.service;

import com.jobtracker.backend.model.*;
import com.jobtracker.backend.repository.JobRepository;
import com.jobtracker.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobEntity createJob(JobEntity job, Authentication authentication) {
    String username = authentication.getName();
    UserEntity user = userRepository.findByUsername(username).orElseThrow();
    job.setUser(user);

    if (job.getDateApplied() == null) {
        job.setDateApplied(LocalDate.now());
    }

    if (job.getProgress() == null) {
        job.setProgress(0);
    }

    if (job.getRounds() == null) {
        job.setRounds(new ArrayList<>());
    }

    return jobRepository.save(job);
}


    public List<JobEntity> getUserJobs(Authentication authentication) {
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        return jobRepository.findByUser(user);
    }

    public void deleteJob(Long jobId, Authentication authentication) {
        System.out.println("Inside JobService deleteJob()");
        String username = authentication.getName();
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    
        if (!job.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this job");
        }
    
        System.out.println("Deleting job for user: " + username);
        jobRepository.delete(job);
    }
    
    

    public JobEntity updateJob(Long jobId, JobEntity updatedJob, Authentication authentication) {
        String username = authentication.getName();
        JobEntity job = jobRepository.findById(jobId).orElseThrow();
        if (!job.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }
        job.setCompany(updatedJob.getCompany());
        job.setPosition(updatedJob.getPosition());
        job.setStatus(updatedJob.getStatus());
        job.setRounds(updatedJob.getRounds());
        return jobRepository.save(job);
    }
}
