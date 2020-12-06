package com.ftn.ues.emailclient.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
public class User extends Identifiable {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Set<Tag> userTags;
    private Set<Account> userAccounts;
    private Set<Contact> userContacts;
}
