package com.ftn.ues.email_client.dao.rest;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@SuperBuilder
public class Message extends DirectMapping<com.ftn.ues.email_client.model.Message> {

    public Message(com.ftn.ues.email_client.model.Message message){
        id = message.getId();
        from = message.getFrom();
        to = message.getTo();
        cc = message.getCc();
        bcc = message.getBcc();
        dateTime = message.getDateTime();
        subject = message.getSubject();
        content = message.getContent();
        unread = message.getUnread();
        attachments = message.getAttachments().stream().map(Attachment::new).collect(Collectors.toSet());
        tags = message.getTags().stream().map(com.ftn.ues.email_client.dao.rest.Tag::new).collect(Collectors.toSet());
        parentFolder = message.getParentFolder() != null ? message.getParentFolder().getId() : null;
        account = message.getAccount().getId();
    }

    @NonNull
    private Long id;

    @NonNull
    private String from;

    @NonNull
    private String to;

    private String cc;

    private String bcc;

    private DateTime dateTime;

    @NonNull
    private String subject;

    @NonNull
    private String content;

    @NonNull
    private Boolean unread;

    @Builder.Default
    private Set<Attachment> attachments = new HashSet<>();

    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    private Long parentFolder;

    @NonNull
    private Long account;

    @Override
    public com.ftn.ues.email_client.model.Message getModelObject() {
        var parentFldr = new Folder();
        parentFldr.setId(parentFolder);
        var acc = new Account();
        acc.setId(account);
        return com.ftn.ues.email_client.model.Message.builder()
                .id(id)
                .from(from)
                .to(to)
                .cc(cc)
                .bcc(bcc)
                .dateTime(dateTime)
                .subject(subject)
                .content(content)
                .unread(unread)
                .attachments(attachments.stream().map(Attachment::getModelObject).collect(Collectors.toSet()))
                .tags(tags.stream().map(Tag::getModelObject).collect(Collectors.toSet()))
                .parentFolder(parentFldr)
                .account(acc)
                .build();
    }
}
