package com.ftn.ues.email_client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.ues.email_client.dao.rest.Contact;
import com.ftn.ues.email_client.repository.database.PhotoRepository;
import com.ftn.ues.email_client.service.ContactService;
import com.ftn.ues.email_client.service.FileStorageService;
import com.ftn.ues.email_client.util.DirectMappingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("api/contact")
public class ContactController {

    @Autowired
    ContactService contactService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    FileStorageService fileStorageService;

    @GetMapping("/photo/{id}")
    public ResponseEntity<InputStreamResource> getContactPhoto(@PathVariable("id") Long id) {
        return photoRepository
                .findById(id)
                .flatMap(p -> fileStorageService.getContactPhoto(p))
                .map(pair -> {
                    var bytes = pair.getValue1();
                    var contentType = pair.getValue0();
                    var in = new ByteArrayInputStream(bytes);
                    return ResponseEntity
                            .ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .body(new InputStreamResource(in));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable("id") Long id) {
        return contactService.getContact(id)
                .map(Contact::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addContact(@RequestParam(value = "contact") String contactJson,
                                             @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            Contact contact = objectMapper.readValue(contactJson, Contact.class);
            com.ftn.ues.email_client.model.Contact modelContact = DirectMappingConverter.toModel(contact);
            modelContact = contactService.addContact(modelContact, photo);
            return ResponseEntity.ok(DirectMappingConverter.toMapping(modelContact, com.ftn.ues.email_client.model.Contact.class, Contact.class));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error while adding contact");
        }


    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateContact(@RequestParam(value = "contact") String contactJson,
                                                @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            Contact contact = objectMapper.readValue(contactJson, Contact.class);
            com.ftn.ues.email_client.model.Contact modelContact = DirectMappingConverter.toModel(contact);
            modelContact = contactService.addContact(modelContact, photo);
            return ResponseEntity.ok(DirectMappingConverter.toMapping(modelContact, com.ftn.ues.email_client.model.Contact.class, Contact.class));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error while adding contact");
        }
    }

    @DeleteMapping("/{id}")
    public Boolean deleteContact(@PathVariable("id") Long id) {
        return contactService.deleteContact(id);
    }

}
