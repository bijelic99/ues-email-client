package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;

import java.util.Set;

public interface RuleService {
    void executeRules(Account account, Set<Long> messagesToExecuteOn);

    void executeRules(Account account, Folder folder, Set<Long> messagesToExecuteOn);
}
