package com.ftn.ues.email_client.dao.elastic_search;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class Attachment extends DirectMapping<com.ftn.ues.email_client.model.Attachment> {

    private Long id;
    private String path;
    private String mimeType;
    private String name;
    private Long message;

    public Attachment(com.ftn.ues.email_client.model.Attachment object) {
        super(object);
        this.id = object.getId();
        this.path = object.getPath();
        this.mimeType = object.getMimeType();
        this.name = object.getName();
        this.message = object.getMessage().getId();
    }

    @Override
    public com.ftn.ues.email_client.model.Attachment getModelObject() {
        return com.ftn.ues.email_client.model.Attachment.builder()
                .id(id)
                .path(path)
                .mimeType(mimeType)
                .name(name)
                .message(Message.builder().id(message).build())
                .build();
    }
}
