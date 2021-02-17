package com.ftn.ues.email_client.controller;

import com.ftn.ues.email_client.client.ContactESClient;
import com.ftn.ues.email_client.client.MessageESClient;
import com.ftn.ues.email_client.client.TagESClient;
import com.ftn.ues.email_client.dao.elastic.Message;
import com.ftn.ues.email_client.dao.rest.User;
import com.ftn.ues.email_client.repository.database.UserRepository;
import com.ftn.ues.email_client.service.MessageIndexService;
import com.ftn.ues.email_client.service.UserService;
import com.ftn.ues.email_client.util.DirectMappingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    MessageIndexService messageIndexService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user){
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
    public ResponseEntity<Object> getAll(){
        try {
            var registeredUsers = repository.findAll();
            return ResponseEntity.ok(DirectMappingConverter.toMapping(registeredUsers, com.ftn.ues.email_client.model.User.class, User.class));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/messages")
    public List<Message> getMessages(@RequestParam Map<String, String> params, @PathVariable("id") Long id){
        return new ArrayList<Message>(messageIndexService.findMessages(id, params));
    }
}
