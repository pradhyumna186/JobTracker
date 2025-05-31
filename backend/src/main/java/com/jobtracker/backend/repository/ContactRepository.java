package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    List<ContactEntity> findByUserId(Long userId);
    List<ContactEntity> findByCompany(String company);
    List<ContactEntity> findByRelationshipType(String relationshipType);
} 