package com.ftn.ues.email_client.controller;

import com.ftn.ues.email_client.dao.rest.Account;
import com.ftn.ues.email_client.dao.rest.Folder;
import com.ftn.ues.email_client.repository.database.AccountRepository;
import com.ftn.ues.email_client.service.AccountService;
import com.ftn.ues.email_client.util.DirectMappingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/{id}/folders")
    public Set<Folder> getAccountFolders(@PathVariable("id") Long id){
        return accountRepository.findById(id)
                .stream()
                .flatMap(account -> account.getFolders().stream())
                .filter(folder -> !folder.getDeleted())
                .map(Folder::new)
                .collect(Collectors.toSet());
    }

    @PostMapping
    public ResponseEntity<Object> addAccount(@RequestBody Account receivedAccount) {
        try {
            var newAccount = accountService.addAccount(DirectMappingConverter.toModel(receivedAccount));
            return ResponseEntity.ok(DirectMappingConverter.toMapping(newAccount, com.ftn.ues.email_client.model.Account.class, Account.class));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAccounts() {
        var accounts = accountService.getAll();
        try {
            return ResponseEntity.ok(DirectMappingConverter.toMapping(accounts, com.ftn.ues.email_client.model.Account.class, Account.class));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") Long id) {
        return accountRepository.findById(id)
                .map(Account::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Boolean deleteAccount(@PathVariable("id") Long id) {
        return accountRepository.findById(id)
                .map(acc -> {
                    acc.setDeleted(true);
                    accountRepository.save(acc);
                    return true;
                })
                .orElse(false);
    }
}
