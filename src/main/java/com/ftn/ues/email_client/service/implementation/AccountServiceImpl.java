package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.repository.database.AccountRepository;
import com.ftn.ues.email_client.repository.database.UserRepository;
import com.ftn.ues.email_client.service.AccountService;
import com.ftn.ues.email_client.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        userRepository.findById(account.getUser().getId()).orElseThrow();
        accountRepository.save(account);
        folderService.fetchFolderStructure(account);
        return accountRepository.getOne(account.getId());
    }

    @Override
    public List<Account> getAll() {
        var accounts = accountRepository.findAll();
        return accounts;
    }
}
