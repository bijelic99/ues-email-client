package com.ftn.ues.email_client.controller;

import com.ftn.ues.email_client.dao.rest.Account;
import com.ftn.ues.email_client.service.AccountService;
import com.ftn.ues.email_client.util.DirectMappingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping
    public ResponseEntity<Object> addAccount(@RequestBody Account receivedAccount){
        try {
            var newAccount = accountService.addAccount(DirectMappingConverter.toModel(receivedAccount));
            return ResponseEntity.ok(DirectMappingConverter.toMapping(newAccount, com.ftn.ues.email_client.model.Account.class, Account.class));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAccounts(){
        var accounts = accountService.getAll();
        try {
            return ResponseEntity.ok(DirectMappingConverter.toMapping(accounts, com.ftn.ues.email_client.model.Account.class, Account.class));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
