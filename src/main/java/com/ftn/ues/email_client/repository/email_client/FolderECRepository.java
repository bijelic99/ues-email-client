package com.ftn.ues.email_client.repository.email_client;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.model.InServerType;
import com.ftn.ues.email_client.model.Message;
import com.ftn.ues.email_client.util.JavaxMailMessageToMessageConverter;
import com.sun.istack.NotNull;
import com.sun.mail.pop3.POP3Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.mail.MessagingException;
import javax.mail.Store;
import java.util.*;

@Repository
public class FolderECRepository {

    @Autowired
    AccountECRepository accountECRepository;

    public Set<Folder> fetchFolderTree(@NotNull Account account) throws MessagingException {
        Store store = accountECRepository.getStore(account);
        store.connect();
        javax.mail.Folder defaultFolder = store.getDefaultFolder();
        switch (account.getInServerType()){
            case POP3:{
                //defaultFolder.open(javax.mail.Folder.READ_ONLY);
                Folder newPop3Folder = Folder.builder()
                        .account(account)
                        .children(new HashSet<>())
                        .messages(new HashSet<>())
                        .name(defaultFolder.getName())
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
