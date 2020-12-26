package com.ftn.ues.email_client.dao.rest;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Contact;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Photo extends DirectMapping<com.ftn.ues.email_client.model.Photo> {

    public Photo(com.ftn.ues.email_client.model.Photo photo){
        id = photo.getId();
        path = photo.getPath();
        contact = photo.getContact().getId();
    }

    @NonNull
    private Long id;

    @NonNull
    private String path;

    @NonNull
    private Long contact;

    @Override
    public com.ftn.ues.email_client.model.Photo getModelObject() {
        var contactObj = new Contact();
        contactObj.setId(contact);
        return com.ftn.ues.email_client.model.Photo.builder()
                .id(id)
                .path(path)
                .contact(contactObj)
                .build();
    }
}
