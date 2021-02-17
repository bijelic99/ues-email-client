package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "folder")
public class Folder extends Identifiable {
    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false, name = "folder_url")
    private String folderUrl;

    @NonNull
    @Builder.Default
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.REMOVE)
    private Set<Folder> children = new HashSet<>();

    @ManyToOne
    private Folder parentFolder;

    private Boolean isMainInbox;

    @Builder.Default
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.REMOVE)
    private Set<Message> messages = new HashSet<>();

    @ManyToOne
    private Account account;
}
