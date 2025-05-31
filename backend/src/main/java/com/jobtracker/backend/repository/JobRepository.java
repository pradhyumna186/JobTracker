package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.JobEntity;
import com.jobtracker.backend.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {
    List<JobEntity> findByUser(UserEntity user);
    List<JobEntity> findTop10ByUserOrderByDateAppliedDesc(UserEntity user);
    List<JobEntity> findTop5ByUserOrderByDateAppliedDesc(UserEntity user);
}
