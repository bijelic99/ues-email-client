package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.model.InServerType;
import com.ftn.ues.email_client.repository.database.MessageRepository;
import com.ftn.ues.email_client.service.MailClientService;
import com.ftn.ues.email_client.util.JavaxMailMessageToMessageConverter;
import com.sun.mail.pop3.POP3Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.mail.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MailClientServiceImpl implements MailClientService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Properties getProperties(@NonNull Account account) {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", account.getSmtpAddress());
        properties.put("mail.smtp.port", account.getSmtpPort());

        switch (account.getInServerType()) {
            case POP3: {
                properties.put("mail.pop3.host", account.getInServerAddress());
                properties.put("mail.pop3.port", account.getInServerPort());
                properties.put("mail.pop3.ssl.enable", "false");
                break;
            }
            case IMAP: {
                properties.put("mail.imap.host", account.getInServerAddress());
                properties.put("mail.imap.port", account.getInServerPort());
                properties.put("mail.imap.ssl.enable", "false");
                break;
            }
            default: {
                break;
            }
        }
        return properties;
    }

    @Override
    public Session getSession(@NonNull Account account) {
        return Session.getInstance(getProperties(account), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account.getUsername(), account.getPassword());
            }
        });
    }

    @Override
    public Store getStore(@NonNull Account account) throws NoSuchProviderException {
        Session session = getSession(account);
        return session.getStore(account.getInServerType() == InServerType.POP3 ? "pop3" : account.getInServerType() == InServerType.IMAP ? "imap" : null);
    }

    @Override
    public Set<Folder> fetchFolderStructure(@NonNull Account account) throws MessagingException {
        switch (account.getInServerType()) {
            case POP3: {
                return fetchPop3FolderStructure(account);
            }
            case IMAP: {
                return fetchImapFolderStructure(account);
            }
            default: {
                return null;
            }
        }
    }

    private Set<Folder> fetchPop3FolderStructure(@NonNull Account account) throws MessagingException {
        var store = (POP3Store) getStore(account);
        store.connect();
        javax.mail.Folder defaultFolder = store.getDefaultFolder();
        Folder newFolder = Folder.builder()
                .folderUrl(defaultFolder.getURLName().toString())
                .account(account)
                .children(new HashSet<>())
                .messages(new HashSet<>())
                .name(defaultFolder.getName())
                .build();
        return new HashSet<>(Collections.singletonList(newFolder));
    }

    private Set<Folder> fetchImapFolderStructure(@NonNull Account account) {
        return null;
    }

    @Override
    public Folder refreshFolder(@NonNull Folder folder) throws MessagingException {
        switch (folder.getAccount().getInServerType()) {
            case POP3: {
                return refreshPop3Folder(folder);
            }
            case IMAP: {
                return refreshImapFolder(folder);
            }
            default: {
                return null;
            }
        }
    }

    private Folder refreshPop3Folder(Folder folder) throws MessagingException {
        var store = (POP3Store) getStore(folder.getAccount());
        store.connect();

        javax.mail.Folder serverFolder = store.getFolder("INBOX");
        serverFolder.open(javax.mail.Folder.READ_ONLY);
        var newMessages = Arrays.stream(serverFolder.getMessages())
                .flatMap(message -> {
                    var resOpt = Optional.empty();
                    try {
                        resOpt = Optional.of(JavaxMailMessageToMessageConverter.convertToMessage(message));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return resOpt.stream().map(o -> (JavaxMailMessageToMessageConverter.ParsedMessage) o);
                })
                .map(parsedMessage ->
                        com.ftn.ues.email_client.model.Message.builder()
                                .id(parsedMessage.getId())
                                .from(parsedMessage.getFrom())
                                .to(parsedMessage.getTo())
                                .cc(parsedMessage.getCc())
                                .bcc(parsedMessage.getBcc())
                                .dateTime(parsedMessage.getDateTime())
                                .subject(parsedMessage.getSubject())
                                .content(parsedMessage.getContent())
                                .attachments(new HashSet<>())
                                .unread(false)
                                .parentFolder(folder)
                                .account(folder.getAccount())
                                .build())
                .collect(Collectors.toList());
        newMessages = messageRepository.saveAll(newMessages);
        folder.getMessages().addAll(newMessages);
        return folder;
    }

    private Folder refreshImapFolder(Folder folder) {
        return null;
    }
}
