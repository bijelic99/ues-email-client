package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
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
    @Builder.Default
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.REMOVE)
    private Set<Folder> children = new HashSet<>();

    @ManyToOne
    private Folder parentFolder;

    @Builder.Default
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.REMOVE)
    private Set<Message> messages = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
    private Set<Rule> rules = new HashSet<>();

    @ManyToOne
    private Account account;
}
