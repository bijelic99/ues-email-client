package com.ftn.ues.emailclient.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;

import java.util.Optional;
import java.util.Set;

@Data
@SuperBuilder
public class Message extends Identifiable{
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private DateTime dateTime;
    private String subject;
    private String content;
    private Boolean unread;
    private Set<Attachment> attachments;
    private Set<Tag> tags;
    private Optional<Folder> parentFolder;
    private Account account;
}
