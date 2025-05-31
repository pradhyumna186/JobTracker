package com.jobtracker.backend.service.impl;

import com.jobtracker.backend.model.ContactEntity;
import com.jobtracker.backend.model.JobEntity;
import com.jobtracker.backend.repository.ContactRepository;
import com.jobtracker.backend.repository.JobRepository;
import com.jobtracker.backend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private JobRepository jobRepository;

    @Override
    public ContactEntity save(ContactEntity entity) {
        return contactRepository.save(entity);
    }

    @Override
    public List<ContactEntity> findAll() {
        return contactRepository.findAll();
    }

    @Override
    public Optional<ContactEntity> findById(Long id) {
        return contactRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        contactRepository.deleteById(id);
    }

    @Override
    public ContactEntity update(Long id, ContactEntity entity) {
        if (contactRepository.existsById(id)) {
            entity.setId(id);
            return contactRepository.save(entity);
        }
        throw new RuntimeException("Contact not found with id: " + id);
    }

    @Override
    public List<ContactEntity> findByUserId(Long userId) {
        return contactRepository.findByUserId(userId);
    }

    @Override
    public List<ContactEntity> findByCompany(String company) {
        return contactRepository.findByCompany(company);
    }

    @Override
    public List<ContactEntity> findByRelationshipType(String relationshipType) {
        return contactRepository.findByRelationshipType(relationshipType);
    }

    @Override
    public ContactEntity addJobToContact(Long contactId, Long jobId) {
        ContactEntity contact = contactRepository.findById(contactId)
            .orElseThrow(() -> new RuntimeException("Contact not found"));
        JobEntity job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found"));
        
        contact.getAssociatedJobs().add(job);
        return contactRepository.save(contact);
    }

    @Override
    public ContactEntity removeJobFromContact(Long contactId, Long jobId) {
        ContactEntity contact = contactRepository.findById(contactId)
            .orElseThrow(() -> new RuntimeException("Contact not found"));
        JobEntity job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found"));
        
        contact.getAssociatedJobs().remove(job);
        return contactRepository.save(contact);
    }
} 