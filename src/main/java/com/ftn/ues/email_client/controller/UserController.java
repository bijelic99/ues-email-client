package com.ftn.ues.email_client.controller;

import com.ftn.ues.email_client.dao.rest.Account;
import com.ftn.ues.email_client.dao.rest.Folder;
import com.ftn.ues.email_client.dao.rest.User;
import com.ftn.ues.email_client.repository.database.FolderRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    MessageIndexService messageIndexService;

    @Autowired
    ContactIndexService contactIndexService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FolderRepository folderRepository;

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

    @GetMapping("/{id}/folders")
    public Set<Folder> getFolders(@PathVariable("id") Long id) {
        return userRepository
                .findById(id)
                .stream()
                .flatMap(user -> user.getUserAccounts().stream().flatMap(account -> account.getFolders().stream()))
                .filter(folder -> !folder.getDeleted())
                .map(Folder::new)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}/messages")
    public Seq<com.ftn.ues.email_client.dao.elastic.Message> getMessages(@RequestParam Map<String, String> params, @PathVariable("id") Long id) {
        return messageIndexService.findMessages(id, params);
    }

    @GetMapping("/{id}/contacts")
    public Seq<com.ftn.ues.email_client.dao.elastic.Contact> getContacts(@RequestParam Map<String, String> params, @PathVariable("id") Long id) {
        return contactIndexService.findContacts(id, params);
    }

    @GetMapping("/{id}/accounts")
    public Set<Account> getAccounts(@PathVariable("id") Long id) {
        return userRepository.findById(id)
                .stream()
                .flatMap(user -> user.getUserAccounts().stream())
                .filter(account -> !account.getDeleted())
                .map(Account::new)
                .collect(Collectors.toSet());
    }
}
