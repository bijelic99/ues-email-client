package com.ftn.ues.email_client.repository.email_client;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.model.InServerType;
import com.ftn.ues.email_client.util.JavaxMailMessageToMessageConverter;
import com.sun.istack.NotNull;
import com.sun.mail.pop3.POP3Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FolderECRepository {

    @Autowired
    AccountECRepository accountECRepository;

    public Set<Folder> fetchFolderTree(@NotNull Account account) throws MessagingException {
        Store store = accountECRepository.getStore(account);
        store.connect();
        javax.mail.Folder storeFolder = store.getDefaultFolder();
        switch (account.getInServerType()){
            case POP3:{
                POP3Folder pop3Folder = (POP3Folder) storeFolder;
                pop3Folder.open(javax.mail.Folder.READ_ONLY);
                javax.mail.Message[] folderMessages = pop3Folder.getMessages();
                Folder newPop3Folder = Folder.builder()
                        .account(account)
                        .children(new HashSet<>())
                        .messages(Arrays.stream(folderMessages).map(JavaxMailMessageToMessageConverter::convertToMessage).collect(Collectors.toSet()))
                        .name(pop3Folder.getName())
                        .build();
                return new HashSet<Folder>(Collections.singletonList(newPop3Folder));
            }
            case IMAP:{
                
                return null;
            }
            default:{
                return null;
            }
        }
    }

}
