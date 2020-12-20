package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.repository.database.FolderRepository;
import com.ftn.ues.email_client.repository.email_client.FolderECRepository;
import com.ftn.ues.email_client.service.FolderService;
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
    FolderECRepository folderECRepository;

    @Override
    public Set<Folder> fetchFolderStructure(Account account) throws MessagingException {
        var folders = folderECRepository.fetchFolderTree(account);
        folders = new HashSet<>(folderRepository.saveAll(folders));
        return folders;
    }
}
