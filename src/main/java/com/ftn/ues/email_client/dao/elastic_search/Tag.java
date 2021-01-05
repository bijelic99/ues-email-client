package com.ftn.ues.email_client.dao.elastic_search;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class Tag extends DirectMapping<com.ftn.ues.email_client.model.Tag> {

    private Long id;
    private String name;
    private Long user;

    @Builder.Default
    private Boolean deleted = false;

    public Tag(com.ftn.ues.email_client.model.Tag object) {
        super(object);
        id = object.getId();
        name = object.getName();
        user = object.getUser().getId();
        deleted = object.getDeleted();
    }

    @Override
    public com.ftn.ues.email_client.model.Tag getModelObject() {
        return com.ftn.ues.email_client.model.Tag.builder()
                .id(id)
                .name(name)
                .user(User.builder().id(user).build())
                .deleted(deleted)
                .build();
    }
}
