package com.ftn.ues.email_client.dao.rest;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@SuperBuilder
public class Contact extends DirectMapping<com.ftn.ues.email_client.model.Contact> {

    public Contact(com.ftn.ues.email_client.model.Contact contact){
        id = contact.getId();
        firstName = contact.getFirstName();
        lastName = contact.getLastName();
        displayName = contact.getDisplayName();
        email = contact.getEmail();
        note = contact.getNote();
        photos = contact.getPhotos().stream().map(Photo::new).collect(Collectors.toSet());
    }

    @NonNull
    private Long id;

    private String firstName;

    private String lastName;

    private String displayName;

    @NonNull
    private String email;

    @NonNull
    private String note;

    @NonNull
    @Builder.Default
    private Set<Photo> photos = new HashSet<>();

    @NonNull
    private Long user;

    @Override
    public com.ftn.ues.email_client.model.Contact getModelObject() {
        var usr = new User();
        usr.setId(user);
        return com.ftn.ues.email_client.model.Contact.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .displayName(displayName)
                .email(email)
                .note(note)
                .photos(photos.stream().map(Photo::getModelObject).collect(Collectors.toSet()))
                .user(usr)
                .build();
    }
}
