package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.ContactDTO;
import com.jobtracker.backend.model.ContactEntity;
import com.jobtracker.backend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contactDTO) {
        ContactEntity contact = convertToEntity(contactDTO);
        ContactEntity savedContact = contactService.save(contact);
        return ResponseEntity.ok(convertToDTO(savedContact));
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> getAllContacts() {
        List<ContactEntity> contacts = contactService.findAll();
        return ResponseEntity.ok(contacts.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        return contactService.findById(id)
            .map(contact -> ResponseEntity.ok(convertToDTO(contact)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ContactDTO>> getContactsByUserId(@PathVariable Long userId) {
        List<ContactEntity> contacts = contactService.findByUserId(userId);
        return ResponseEntity.ok(contacts.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/company/{company}")
    public ResponseEntity<List<ContactDTO>> getContactsByCompany(@PathVariable String company) {
        List<ContactEntity> contacts = contactService.findByCompany(company);
        return ResponseEntity.ok(contacts.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/type/{relationshipType}")
    public ResponseEntity<List<ContactDTO>> getContactsByRelationshipType(@PathVariable String relationshipType) {
        List<ContactEntity> contacts = contactService.findByRelationshipType(relationshipType);
        return ResponseEntity.ok(contacts.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable Long id, @RequestBody ContactDTO contactDTO) {
        ContactEntity contact = convertToEntity(contactDTO);
        ContactEntity updatedContact = contactService.update(id, contact);
        return ResponseEntity.ok(convertToDTO(updatedContact));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{contactId}/jobs/{jobId}")
    public ResponseEntity<ContactDTO> addJobToContact(@PathVariable Long contactId, @PathVariable Long jobId) {
        ContactEntity contact = contactService.addJobToContact(contactId, jobId);
        return ResponseEntity.ok(convertToDTO(contact));
    }

    @DeleteMapping("/{contactId}/jobs/{jobId}")
    public ResponseEntity<ContactDTO> removeJobFromContact(@PathVariable Long contactId, @PathVariable Long jobId) {
        ContactEntity contact = contactService.removeJobFromContact(contactId, jobId);
        return ResponseEntity.ok(convertToDTO(contact));
    }

    private ContactDTO convertToDTO(ContactEntity contact) {
        ContactDTO dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setCompany(contact.getCompany());
        dto.setRole(contact.getRole());
        dto.setEmail(contact.getEmail());
        dto.setLinkedinUrl(contact.getLinkedinUrl());
        dto.setRelationshipType(contact.getRelationshipType());
        dto.setNotes(contact.getNotes());
        dto.setLastContactDate(contact.getLastContactDate());
        dto.setLastContactNotes(contact.getLastContactNotes());
        if (contact.getUser() != null) {
            dto.setUserId(contact.getUser().getId());
        }
        return dto;
    }

    private ContactEntity convertToEntity(ContactDTO dto) {
        ContactEntity contact = new ContactEntity();
        contact.setId(dto.getId());
        contact.setName(dto.getName());
        contact.setCompany(dto.getCompany());
        contact.setRole(dto.getRole());
        contact.setEmail(dto.getEmail());
        contact.setLinkedinUrl(dto.getLinkedinUrl());
        contact.setRelationshipType(dto.getRelationshipType());
        contact.setNotes(dto.getNotes());
        contact.setLastContactDate(dto.getLastContactDate());
        contact.setLastContactNotes(dto.getLastContactNotes());
        return contact;
    }
} 