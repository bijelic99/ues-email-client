package com.ftn.ues.email_client.dao.rest;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Contact;
import com.ftn.ues.email_client.model.Identifiable;
import com.ftn.ues.email_client.model.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@NoArgsConstructor
public class User extends DirectMapping<com.ftn.ues.email_client.model.User> {

    public User(com.ftn.ues.email_client.model.User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        userTags = user.getUserTags().stream().map(Identifiable::getId).collect(Collectors.toSet());
        userAccounts = user.getUserAccounts().stream().map(Identifiable::getId).collect(Collectors.toSet());
        userContacts = user.getUserContacts().stream().map(Identifiable::getId).collect(Collectors.toSet());
    }

    @NonNull
    private Long id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    private String firstName;

    private String lastName;

    @Builder.Default
    private Set<Long> userTags = new HashSet<>();

    @Builder.Default
    private Set<Long> userAccounts = new HashSet<>();

    @Builder.Default
    private Set<Long> userContacts = new HashSet<>();

    @Override
    public com.ftn.ues.email_client.model.User getModelObject() {
        return com.ftn.ues.email_client.model.User.builder()
                .id(id)
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .userTags(userTags.stream().map(tag -> {
                    var t = new Tag();
                    t.setId(tag);
                    return t;
                }).collect(Collectors.toSet()))
                .userAccounts(userAccounts.stream().map(account -> {
                    var a = new com.ftn.ues.email_client.model.Account();
                    a.setId(account);
                    return a;
                }).collect(Collectors.toSet()))
                .userContacts(userContacts.stream().map(contact -> {
                    var c = new Contact();
                    c.setId(contact);
                    return c;
                }).collect(Collectors.toSet()))
                .build();

    }
}
