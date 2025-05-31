package com.jobtracker.backend.service;

import com.jobtracker.backend.model.DocumentEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface DocumentService extends BaseService<DocumentEntity, Long> {
    DocumentEntity uploadDocument(MultipartFile file, DocumentEntity document);
    List<DocumentEntity> findByUserId(Long userId);
    List<DocumentEntity> findByUserIdAndType(Long userId, String type);
    List<DocumentEntity> findTemplates(Long userId);
    List<DocumentEntity> findByJobId(Long jobId);
    List<DocumentEntity> searchByName(Long userId, String name);
    void deleteDocument(Long id);
    byte[] downloadDocument(Long id);
} 