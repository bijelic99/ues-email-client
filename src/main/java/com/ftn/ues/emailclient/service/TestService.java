package com.ftn.ues.emailclient.service;

import com.ftn.ues.emailclient.dao.elastic_search.Contact;
import com.ftn.ues.emailclient.repository.elastic_search.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api")
public class TestService {
    @Autowired
    private ContactRepository contactRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Contact addC(@RequestBody Contact contact){
        contactRepository.save(contact);
        return contact;
    }
}
