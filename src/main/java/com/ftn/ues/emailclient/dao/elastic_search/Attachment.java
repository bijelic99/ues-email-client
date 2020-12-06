package com.ftn.ues.emailclient.dao.elastic_search;

import com.ftn.ues.emailclient.dao.DirectMapping;
import com.ftn.ues.emailclient.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class Attachment extends DirectMapping<com.ftn.ues.emailclient.model.Attachment> {

    private String path;
    private String mimeType;
    private String name;
    private Long message;

    public Attachment(com.ftn.ues.emailclient.model.Attachment object) {
        super(object);
        this.path = object.getPath();
        this.mimeType = object.getMimeType();
        this.name = object.getName();
        this.message = object.getMessage().getId();
    }

    @Override
    public com.ftn.ues.emailclient.model.Attachment getModelObject() {
        return com.ftn.ues.emailclient.model.Attachment.builder()
                .id(id)
                .path(path)
                .mimeType(mimeType)
                .name(name)
                .message(Message.builder().id(message).build())
                .build();
    }
}
