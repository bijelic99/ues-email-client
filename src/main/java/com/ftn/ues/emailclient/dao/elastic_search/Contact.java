package com.ftn.ues.emailclient.dao.elastic_search;

import com.ftn.ues.emailclient.dao.DirectMapping;
import com.ftn.ues.emailclient.model.Identifiable;
import com.ftn.ues.emailclient.model.Photo;
import com.ftn.ues.emailclient.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@SuperBuilder
@Document(indexName = "contacts")
public class Contact extends DirectMapping<com.ftn.ues.emailclient.model.Contact> {

    private String firstName;
    private String lastName;
    private String displayName;
    private String email;
    private String note;
    private Set<Long> photos;
    private Long user;

    public Contact(com.ftn.ues.emailclient.model.Contact object) {
        super(object);
        firstName = object.getFirstName();
        lastName = object.getLastName();
        displayName = object.getDisplayName();
        email = object.getEmail();
        note = object.getNote();
        photos = object.getPhotos().stream().map(Identifiable::getId).collect(Collectors.toSet());
        user = object.getUser().getId();
    }

    @Override
    public com.ftn.ues.emailclient.model.Contact getModelObject() {
        return com.ftn.ues.emailclient.model.Contact.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .displayName(displayName)
                .email(email)
                .note(note)
                .photos(photos.stream()
                        .map(x -> Photo.builder().id(x).build()).collect(Collectors.toSet())
                )
                .user(User.builder().id(user).build())
                .build();
    }
}
