package com.ftn.ues.email_client.dao.rest;


import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Message;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Attachment extends DirectMapping<com.ftn.ues.email_client.model.Attachment> {

    public Attachment(com.ftn.ues.email_client.model.Attachment attachment){
        id = attachment.getId();
        path = attachment.getPath();
        mimeType = attachment.getMimeType();
        name = attachment.getName();
        message = attachment.getMessage().getId();
    }

    @NonNull
    private Long id;

    @NonNull
    private String path;

    @NonNull
    private String mimeType;

    @NonNull
    private String name;

    @NonNull
    private Long message;

    @Override
    public com.ftn.ues.email_client.model.Attachment getModelObject() {
        var msg = new Message();
        msg.setId(message);
        return com.ftn.ues.email_client.model.Attachment.builder()
                .id(id)
                .path(path)
                .mimeType(mimeType)
                .name(name)
                .message(msg)
                .build();
    }
}
