package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends Identifiable {
    @NonNull
    @Column(nullable = false)
    private String username;

    @NonNull
    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Tag> userTags = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Account> userAccounts = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Contact> userContacts = new HashSet<>();
}
