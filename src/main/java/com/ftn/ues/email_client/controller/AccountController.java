package com.ftn.ues.email_client.controller;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping
    public ResponseEntity<Object> addAccount(@RequestBody Account account){
        try {
            return ResponseEntity.ok(accountService.addAccount(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAccounts(){
        return ResponseEntity.ok(accountService.getAll());
    }
}
