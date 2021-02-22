package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message extends Identifiable{

    @Column(name = "message_uid")
    private String messageUid;

    @NonNull
    @Column(name = "_from", nullable = false)
    private String from;

    @NonNull
    @Column(name = "_to", nullable = false)
    private String to;

    @Column
    private String cc;

    @Column
    private String bcc;

    @Column(name = "date_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateTime;

    @Column
    private String subject;

    @NonNull
    @Lob
    @Column(nullable = false)
    private String content;

    @NonNull
    @Column(nullable = false)
    private Boolean unread;

    @Builder.Default
    @OneToMany(mappedBy = "message")
    private Set<Attachment> attachments = new HashSet<>();

    @Builder.Default
    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "parent_folder")
    private Folder parentFolder;

    @NonNull
    @ManyToOne
    private Account account;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return Message.builder()
                .id(this.id)
                .messageUid(this.messageUid)
                .from(this.from)
                .to(this.to)
                .cc(this.cc)
                .bcc(this.bcc)
                .dateTime(this.dateTime)
                .subject(this.subject)
                .content(this.content)
                .unread(this.unread)
                .attachments(this.attachments.stream().map(attachment -> {
                    try {
                        return (Attachment)attachment.clone();
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toSet()))
                .tags(this.tags)
                .parentFolder(this.parentFolder)
                .account(this.account)
                .build();
    }

    public String getMessageUid() {
        return messageUid;
    }

    public void setMessageUid(String messageUid) {
        this.messageUid = messageUid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
