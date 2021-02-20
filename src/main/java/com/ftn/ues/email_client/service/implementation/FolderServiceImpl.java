package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.*;
import com.ftn.ues.email_client.repository.database.FolderRepository;
import com.ftn.ues.email_client.repository.database.MessageRepository;
import com.ftn.ues.email_client.service.FolderService;
import com.ftn.ues.email_client.service.MailClientService;
import org.checkerframework.checker.units.qual.A;
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

    @Autowired
    Pop3IngestService pop3IngestService;

    @Autowired
    ImapIngestService imapIngestService;

    @Override
    public Set<Folder> fetchFolderStructure(Account account) throws MessagingException {
        var folders = mailClientService.fetchFolderStructure(account);
        folders = new HashSet<>(folderRepository.saveAll(folders));
        return folders;
    }

    @Override
    public Folder refreshFolder(Long id) throws MessagingException {
        var folder = folderRepository.findById(id).orElseThrow();
        if(folder.getAccount().getInServerType().equals(InServerType.POP3))
            pop3IngestService.ingestNewMail(new Pop3IngestServiceParams(folder.getAccount()));
        else imapIngestService.ingestNewMail(new ImapIngestServiceParams(folder.getAccount(), folder));
        folder = folderRepository.findById(id).orElseThrow();
        return folder;
    }
}
