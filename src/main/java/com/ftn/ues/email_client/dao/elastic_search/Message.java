package com.ftn.ues.email_client.dao.elastic_search;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@SuperBuilder
@Document(indexName = "message", shards = 2)
public class Message extends DirectMapping<com.ftn.ues.email_client.model.Message> {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "serbian", searchAnalyzer = "serbian")
    private String from;

    @Field(type = FieldType.Text, analyzer = "serbian", searchAnalyzer = "serbian")
    private String to;

    private String cc;

    private String bcc;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSZZ")
    private DateTime dateTime;

    @Field(type = FieldType.Text, analyzer = "serbian", searchAnalyzer = "serbian")
    private String subject;

    @Field(type = FieldType.Text, analyzer = "serbian", searchAnalyzer = "serbian")
    private String content;

    private Boolean unread;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<Attachment> attachments = new HashSet<>();

    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<Tag> Tags;

    private Long parentFolder;

    private Long account;

    @Builder.Default
    private Boolean deleted = false;

    @Field(type = FieldType.Text, analyzer = "serbian", searchAnalyzer = "serbian")
    private String abstractText;

    @Field(type = FieldType.Text, analyzer = "serbian", searchAnalyzer = "serbian")
    private String fullText;

    public Message(com.ftn.ues.email_client.model.Message object) {
        super(object);
        id = object.getId();
        from = object.getFrom();
        to = object.getTo();
        cc = object.getCc();
        bcc = object.getBcc();
        dateTime = object.getDateTime();
        subject = object.getSubject();
        content = object.getContent();
        unread = object.getUnread();
        Tags = object.getTags().stream().map(Tag::new).collect(Collectors.toSet());
        account = object.getAccount().getId();
        deleted = object.getDeleted();
        abstractText = (content.length() < 1000 ? content : content.substring(0, 1000));
        fullText = new StringBuilder()
                .append(subject)
                .append(content)
                .toString();
    }

    @Override
    public com.ftn.ues.email_client.model.Message getModelObject() {
        return com.ftn.ues.email_client.model.Message.builder()
                .id(id)
                .from(from)
                .to(to)
                .cc(cc)
                .bcc(bcc)
                .dateTime(dateTime)
                .subject(subject)
                .content(content)
                .unread(unread)
                .attachments(attachments.stream().map(Attachment::getModelObject).collect(Collectors.toSet()))
                .tags(Tags.stream().map(Tag::getModelObject).collect(Collectors.toSet()))
                .account(Account.builder().id(account).build())
                .parentFolder(Folder.builder().id(parentFolder).build())
                .deleted(deleted)
                .build();
    }
}
