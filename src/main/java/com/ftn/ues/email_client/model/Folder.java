package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "folder")
public class Folder extends Identifiable {
    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.REMOVE)
    private Set<Folder> children;

    @ManyToOne
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.REMOVE)
    private Set<Message> messages;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
    private Set<Rule> rules;

    @ManyToOne
    private Account account;
}
