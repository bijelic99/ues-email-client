package com.ftn.ues.email_client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.ues.email_client.dao.rest.Contact;
import com.ftn.ues.email_client.service.ContactService;
import com.ftn.ues.email_client.util.DirectMappingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;

@RestController
@RequestMapping("api/contact")
public class ContactController {

    @Autowired
    ContactService contactService;

    @Autowired
    ObjectMapper objectMapper;

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

    public Contact updateContact(Contact contact) {
        return null;
    }

    public Boolean deleteContact(Long id) {
        return null;
    }
}
