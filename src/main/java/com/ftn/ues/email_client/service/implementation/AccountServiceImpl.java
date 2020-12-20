package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.repository.database.AccountRepository;
import com.ftn.ues.email_client.repository.database.UserRepository;
import com.ftn.ues.email_client.service.AccountService;
import com.ftn.ues.email_client.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FolderService folderService;

    @Override
    public Account addAccount(Account account) throws Exception {
        var userOpt = userRepository.findById(account.getUser().getId());
        if (userOpt.isEmpty()) throw new Exception("Cannot add an account for non existing user");
        account = accountRepository.save(account);
        folderService.fetchFolderStructure(account);
        return account;
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }
}
