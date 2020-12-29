package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "date_time", nullable = false) @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
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
    @OneToMany(mappedBy = "message", orphanRemoval = true)
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
}
