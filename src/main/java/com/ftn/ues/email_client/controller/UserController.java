package com.ftn.ues.email_client.controller;

import com.ftn.ues.email_client.dao.rest.User;
import com.ftn.ues.email_client.repository.database.UserRepository;
import com.ftn.ues.email_client.service.ContactIndexService;
import com.ftn.ues.email_client.service.MessageIndexService;
import com.ftn.ues.email_client.service.UserService;
import com.ftn.ues.email_client.util.DirectMappingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scala.collection.immutable.Seq;

import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    MessageIndexService messageIndexService;

    @Autowired
    ContactIndexService contactIndexService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        try {
            var registeredUser = userService.register(DirectMappingConverter.toModel(user));
            return ResponseEntity.ok(DirectMappingConverter.toMapping(registeredUser, com.ftn.ues.email_client.model.User.class, User.class));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Autowired
    UserRepository repository;

    @GetMapping()
    public ResponseEntity<Object> getAll() {
        try {
            var registeredUsers = repository.findAll();
            return ResponseEntity.ok(DirectMappingConverter.toMapping(registeredUsers, com.ftn.ues.email_client.model.User.class, User.class));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/messages")
    public Seq<com.ftn.ues.email_client.dao.elastic.Message> getMessages(@RequestParam Map<String, String> params, @PathVariable("id") Long id) {
        return messageIndexService.findMessages(id, params);
    }

    @GetMapping("/{id}/contacts")
    public Seq<com.ftn.ues.email_client.dao.elastic.Contact> getContacts(@RequestParam Map<String, String> params, @PathVariable("id") Long id) {
        return contactIndexService.findContacts(id, params);
    }
}
