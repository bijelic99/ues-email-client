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

    @NonNull
    @Column(nullable = false)
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
}
