package com.ftn.ues.email_client.dao.elastic_search;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.AttachmentRaw;
import com.ftn.ues.email_client.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.javatuples.Pair;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Arrays;
import java.util.Base64;

@Data
@AllArgsConstructor
@SuperBuilder
@Document(indexName = "attachment", shards = 2)
public class Attachment extends DirectMapping<com.ftn.ues.email_client.model.Attachment> {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String path;

    @Field(type = FieldType.Text)
    private String mimeType;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Long)
    private Long message;

    @Field(type = FieldType.Text)
    private String data;

    @Field(type = FieldType.Nested)
    private AttachmentMetadata metadata;

    @Builder.Default
    private Boolean deleted = false;

    public Attachment(Pair<com.ftn.ues.email_client.model.Attachment, AttachmentRaw> attachmentRawPair) {
        this(attachmentRawPair.getValue0());
        this.data = Base64.getEncoder().encodeToString(attachmentRawPair.getValue1().getData());
    }

    public Attachment(com.ftn.ues.email_client.model.Attachment object) {
        super(object);
        this.id = object.getId();
        this.path = object.getPath();
        this.mimeType = object.getMimeType();
        this.name = object.getName();
        this.message = object.getMessage().getId();
        this.deleted = object.getDeleted();
    }

    @Override
    public com.ftn.ues.email_client.model.Attachment getModelObject() {
        return com.ftn.ues.email_client.model.Attachment.builder()
                .id(id)
                .path(path)
                .mimeType(mimeType)
                .name(name)
                .message(Message.builder().id(message).build())
                .deleted(deleted)
                .build();
    }

    @Data
    @AllArgsConstructor
    public class AttachmentMetadata {

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSZZ")
        private DateTime date;

        @Field(name = "content_type")
        private String contentType;

        private String language;
        private String content;

        @Field(name = "content_length")
        private Long contentLength;

    }
}
