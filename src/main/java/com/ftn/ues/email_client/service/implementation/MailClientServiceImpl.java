package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.model.Message;
import com.ftn.ues.email_client.model.*;
import com.ftn.ues.email_client.repository.database.AttachmentRepository;
import com.ftn.ues.email_client.repository.database.MessageRepository;
import com.ftn.ues.email_client.service.FileStorageService;
import com.ftn.ues.email_client.service.IndexingService;
import com.ftn.ues.email_client.service.MailClientService;
import com.ftn.ues.email_client.util.JavaxMailMessageToMessageConverter;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class MailClientServiceImpl implements MailClientService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    IndexingService indexingService;

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

    @Override
    public Message sendMessage(Message message) throws MessagingException {
        var account = message.getAccount();
        var session = getSession(account);
        var mimeMessage = new MimeMessage(session);

        mimeMessage.setFrom(account.getMailAddress());

        mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, message.getTo());
        mimeMessage.setRecipients(javax.mail.Message.RecipientType.CC, message.getCc());
        mimeMessage.setRecipients(javax.mail.Message.RecipientType.BCC, message.getBcc());

        mimeMessage.setSubject(message.getSubject());
        var content = new MimeMultipart();
        var textPart = new MimeBodyPart();
        textPart.setText(message.getContent(), "utf-8");

        content.addBodyPart(textPart);

         fileStorageService.getAttachments(message.getAttachments()).stream()
                .flatMap(attachmentData -> {
                    var returnOpt = Optional.empty();
                    try {
                        var attMpart = new MimeBodyPart();
                        attMpart.setFileName(attachmentData.getFilename());
                        DataSource source = new ByteArrayDataSource(attachmentData.getData(), attachmentData.getMimeType());
                        attMpart.setDataHandler(new DataHandler(source));
                        returnOpt = Optional.of(attMpart);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    return returnOpt.stream().map(o -> (MimeBodyPart) o);
                }).forEach(mimeBodyPart -> {
                    try {
                        content.addBodyPart(mimeBodyPart);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                });
         var currentDate = new Date();
         mimeMessage.setSentDate(currentDate);
         message.setDateTime(new DateTime(currentDate.getTime()));

         mimeMessage.setContent(content);

         Transport.send(mimeMessage);

        return message;
    }

    private Folder refreshPop3Folder(Folder folder) throws MessagingException {
        var store = (POP3Store) getStore(folder.getAccount());
        store.connect();

        POP3Folder serverFolder = (POP3Folder) store.getFolder("INBOX");
        serverFolder.open(javax.mail.Folder.READ_ONLY);

        var newMessages = Arrays.asList(serverFolder.getMessages())
                .stream()
                .flatMap(message -> {
                    var resOpt = Optional.empty();
                    try {
                        resOpt = Optional.of(JavaxMailMessageToMessageConverter.convertToMessage(message, serverFolder.getUID(message)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return resOpt.stream().map(o -> (JavaxMailMessageToMessageConverter.ParsedMessage) o);
                })
                .map(parsedMessage -> {
                    var message = com.ftn.ues.email_client.model.Message.builder()
                            .id(null)
                            .messageUid(parsedMessage.getId())
                            .from(parsedMessage.getFrom())
                            .to(parsedMessage.getTo())
                            .cc(parsedMessage.getCc())
                            .bcc(parsedMessage.getBcc())
                            .dateTime(parsedMessage.getDateTime())
                            .subject(parsedMessage.getSubject())
                            .content(parsedMessage.getContent())
                            .unread(false)
                            .parentFolder(folder)
                            .account(folder.getAccount())
                            .build();
                    var attachments = parsedMessage.getAttachments().stream()
                            .flatMap(attachmentData -> {
                                var resOpt = Optional.empty();
                                try {
                                    var path = fileStorageService.addAttachment(attachmentData);
                                    resOpt = Optional.of(Attachment.builder()
                                            .id(null)
                                            .path(path)
                                            .mimeType(attachmentData.getMimeType())
                                            .name(attachmentData.getFilename())
                                            .message(message)
                                            .build());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return resOpt.stream().map(o -> (Attachment) o);
                            }).collect(Collectors.toSet());
                    message.setAttachments(attachments);
                    return message;
                })
                .filter(message -> folder.getMessages()
                        .stream().noneMatch(m -> m.getMessageUid().equals(message.getMessageUid()))
                )
                .collect(Collectors.toList());

        newMessages = messageRepository.saveAll(newMessages);
        var attachments = newMessages.stream().map(Message::getAttachments).flatMap(Collection::stream).collect(Collectors.toList());
        attachmentRepository.saveAll(attachments);
        newMessages = messageRepository.findAllById(newMessages.stream().map(Identifiable::getId).collect(Collectors.toSet()));

        if (!indexingService.indexMessage(newMessages.toArray(Message[]::new))) log.error("Indexing unsuccessful");

        folder.getMessages().addAll(newMessages);
        return folder;
    }

    private Folder refreshImapFolder(Folder folder) {
        return null;
    }
}
