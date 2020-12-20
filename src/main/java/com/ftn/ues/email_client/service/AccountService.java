package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Account;

import java.util.List;
import java.util.Set;

public interface AccountService {
    public Account addAccount(Account account) throws Exception;

    public List<Account> getAll();
}
