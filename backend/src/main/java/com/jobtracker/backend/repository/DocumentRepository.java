package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findByUserId(Long userId);
    List<DocumentEntity> findByUserIdAndType(Long userId, String type);
    List<DocumentEntity> findByUserIdAndIsTemplate(Long userId, boolean isTemplate);
    List<DocumentEntity> findByJobId(Long jobId);
    List<DocumentEntity> findByUserIdAndNameContaining(Long userId, String name);
} 