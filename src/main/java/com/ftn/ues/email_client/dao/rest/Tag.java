package com.ftn.ues.email_client.dao.rest;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.User;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Tag extends DirectMapping<com.ftn.ues.email_client.model.Tag> {

    public Tag(com.ftn.ues.email_client.model.Tag tag){
        id = tag.getId();
        name = tag.getName();
        user = tag.getUser().getId();
    }

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Long user;

    @Override
    public com.ftn.ues.email_client.model.Tag getModelObject() {
        var user = new User();
        user.setId(this.id);
        return com.ftn.ues.email_client.model.Tag.builder()
                .id(id)
                .name(name)
                .user(user)
                .build();
    }
}
