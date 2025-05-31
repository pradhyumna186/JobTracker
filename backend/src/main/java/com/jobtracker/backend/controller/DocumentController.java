package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.DocumentDTO;
import com.jobtracker.backend.model.DocumentEntity;
import com.jobtracker.backend.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "isTemplate", defaultValue = "false") boolean isTemplate,
            @RequestParam(value = "description", required = false) String description) {
        
        DocumentEntity document = new DocumentEntity();
        document.setName(name);
        document.setType(type);
        document.setIsTemplate(isTemplate);
        document.setDescription(description);
        
        DocumentEntity savedDocument = documentService.uploadDocument(file, document);
        return ResponseEntity.ok(convertToDTO(savedDocument));
    }

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        List<DocumentEntity> documents = documentService.findAll();
        return ResponseEntity.ok(documents.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        return documentService.findById(id)
            .map(document -> ResponseEntity.ok(convertToDTO(document)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByUserId(@PathVariable Long userId) {
        List<DocumentEntity> documents = documentService.findByUserId(userId);
        return ResponseEntity.ok(documents.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByUserIdAndType(
            @PathVariable Long userId, @PathVariable String type) {
        List<DocumentEntity> documents = documentService.findByUserIdAndType(userId, type);
        return ResponseEntity.ok(documents.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}/templates")
    public ResponseEntity<List<DocumentDTO>> getTemplates(@PathVariable Long userId) {
        List<DocumentEntity> documents = documentService.findTemplates(userId);
        return ResponseEntity.ok(documents.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByJobId(@PathVariable Long jobId) {
        List<DocumentEntity> documents = documentService.findByJobId(jobId);
        return ResponseEntity.ok(documents.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<DocumentDTO>> searchDocuments(
            @PathVariable Long userId, @RequestParam String name) {
        List<DocumentEntity> documents = documentService.searchByName(userId, name);
        return ResponseEntity.ok(documents.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadDocument(@PathVariable Long id) {
        DocumentEntity document = documentService.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found"));
        
        byte[] data = documentService.downloadDocument(id);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + document.getName())
            .contentType(MediaType.parseMediaType(document.getFileType()))
            .contentLength(data.length)
            .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    private DocumentDTO convertToDTO(DocumentEntity document) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setName(document.getName());
        dto.setType(document.getType());
        dto.setFilePath(document.getFilePath());
        dto.setFileType(document.getFileType());
        dto.setUploadDate(document.getUploadDate());
        dto.setVersion(document.getVersion());
        dto.setIsTemplate(document.isTemplate());
        dto.setDescription(document.getDescription());
        if (document.getUser() != null) {
            dto.setUserId(document.getUser().getId());
        }
        if (document.getJob() != null) {
            dto.setJobId(document.getJob().getId());
        }
        return dto;
    }
} 