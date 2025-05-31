package com.jobtracker.backend.service;

import com.jobtracker.backend.model.ContactEntity;
import java.util.List;

public interface ContactService extends BaseService<ContactEntity, Long> {
    List<ContactEntity> findByUserId(Long userId);
    List<ContactEntity> findByCompany(String company);
    List<ContactEntity> findByRelationshipType(String relationshipType);
    ContactEntity addJobToContact(Long contactId, Long jobId);
    ContactEntity removeJobFromContact(Long contactId, Long jobId);
} 