package com.ftn.ues.email_client.dao.elastic_search;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Identifiable;
import com.ftn.ues.email_client.model.Photo;
import com.ftn.ues.email_client.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@SuperBuilder
@Document(indexName = "contact", shards = 2)
public class Contact extends DirectMapping<com.ftn.ues.email_client.model.Contact> {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String displayName;
    private String email;
    private String note;
    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<Long> photos;
    private Long user;

    @Builder.Default
    private Boolean deleted = false;

    public Contact(com.ftn.ues.email_client.model.Contact object) {
        super(object);
        id = object.getId();
        firstName = object.getFirstName();
        lastName = object.getLastName();
        displayName = object.getDisplayName();
        email = object.getEmail();
        note = object.getNote();
        photos = object.getPhotos().stream().map(Identifiable::getId).collect(Collectors.toSet());
        user = object.getUser().getId();
        deleted = object.getDeleted();
    }

    @Override
    public com.ftn.ues.email_client.model.Contact getModelObject() {
        return com.ftn.ues.email_client.model.Contact.builder()
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
                .deleted(deleted)
                .build();
    }
}
