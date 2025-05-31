package com.jobtracker.backend.service.impl;

import com.jobtracker.backend.model.DocumentEntity;
import com.jobtracker.backend.repository.DocumentRepository;
import com.jobtracker.backend.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public DocumentEntity save(DocumentEntity entity) {
        return documentRepository.save(entity);
    }

    @Override
    public List<DocumentEntity> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Optional<DocumentEntity> findById(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        DocumentEntity document = documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found"));
        
        // Delete the file from storage
        try {
            Path filePath = Paths.get(document.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage());
        }
        
        documentRepository.deleteById(id);
    }

    @Override
    public DocumentEntity update(Long id, DocumentEntity entity) {
        if (documentRepository.existsById(id)) {
            entity.setId(id);
            return documentRepository.save(entity);
        }
        throw new RuntimeException("Document not found with id: " + id);
    }

    @Override
    public DocumentEntity uploadDocument(MultipartFile file, DocumentEntity document) {
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Save file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // Update document entity
            document.setFilePath(filePath.toString());
            document.setFileType(file.getContentType());
            document.setUploadDate(LocalDateTime.now());
            
            return documentRepository.save(document);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    @Override
    public List<DocumentEntity> findByUserId(Long userId) {
        return documentRepository.findByUserId(userId);
    }

    @Override
    public List<DocumentEntity> findByUserIdAndType(Long userId, String type) {
        return documentRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public List<DocumentEntity> findTemplates(Long userId) {
        return documentRepository.findByUserIdAndIsTemplate(userId, true);
    }

    @Override
    public List<DocumentEntity> findByJobId(Long jobId) {
        return documentRepository.findByJobId(jobId);
    }

    @Override
    public List<DocumentEntity> searchByName(Long userId, String name) {
        return documentRepository.findByUserIdAndNameContaining(userId, name);
    }

    @Override
    public void deleteDocument(Long id) {
        deleteById(id);
    }

    @Override
    public byte[] downloadDocument(Long id) {
        DocumentEntity document = documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found"));
        
        try {
            Path filePath = Paths.get(document.getFilePath());
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage());
        }
    }
} 