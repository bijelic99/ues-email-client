package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.repository.database.FolderRepository;
import com.ftn.ues.email_client.service.FolderService;
import com.ftn.ues.email_client.service.MailClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.Set;

@Service
public class FolderServiceImpl implements FolderService {
    @Autowired
    FolderRepository folderRepository;

    @Autowired
    MailClientService mailClientService;

    @Override
    public Set<Folder> fetchFolderStructure(Account account) throws MessagingException {
        var folders = mailClientService.fetchFolderStructure(account);
        folders = new HashSet<>(folderRepository.saveAll(folders));
        return folders;
    }

    @Override
    public Folder refreshFolder(Long id) throws MessagingException {
        var folder = folderRepository.findById(id).orElseThrow();
        folder = mailClientService.refreshFolder(folder);
        folder = executeRules(folder);
        return folderRepository.save(folder);
    }

    @Override
    public Folder executeRules(Folder folder) {
        // TODO implement
        return folder;
    }
}
