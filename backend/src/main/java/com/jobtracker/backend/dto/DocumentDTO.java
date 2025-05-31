package com.jobtracker.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentDTO {
    private Long id;
    private String name;
    private String type;
    private String filePath;
    private String fileType;
    private LocalDateTime uploadDate;
    private String version;
    private boolean isTemplate;
    private String description;
    private Long userId;
    private Long jobId;
} 