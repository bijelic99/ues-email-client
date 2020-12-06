package com.ftn.ues.emailclient.dao.elastic_search;

import com.ftn.ues.emailclient.dao.DirectMapping;
import com.ftn.ues.emailclient.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class Tag extends DirectMapping<com.ftn.ues.emailclient.model.Tag> {

    private String name;
    private Long user;

    public Tag(com.ftn.ues.emailclient.model.Tag object) {
        super(object);
        name = object.getName();
        user = object.getUser().getId();
    }

    @Override
    public com.ftn.ues.emailclient.model.Tag getModelObject() {
        return com.ftn.ues.emailclient.model.Tag.builder()
                .id(id)
                .name(name)
                .user(User.builder().id(user).build())
                .build();
    }
}
