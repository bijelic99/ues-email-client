package com.ftn.ues.email_client.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message extends Identifiable{
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
    private DateTime dateTime;

    @NonNull
    @Column(nullable = false)
    private String subject;

    @NonNull
    @Column(nullable = false)
    private String content;

    @NonNull
    @Column(nullable = false)
    private Boolean unread;

    @OneToMany(mappedBy = "message", orphanRemoval = true)
    private Set<Attachment> attachments;

    @ManyToMany
    private Set<Tag> tags;

    @Column(name = "parent_folder")
    private String parentFolder;

    @NonNull
    @ManyToOne
    private Account account;
}
