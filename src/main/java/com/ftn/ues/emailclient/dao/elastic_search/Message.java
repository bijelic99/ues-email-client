package com.ftn.ues.emailclient.dao.elastic_search;

import com.ftn.ues.emailclient.dao.DirectMapping;
import com.ftn.ues.emailclient.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@SuperBuilder
@Document(indexName = "messages")
public class Message extends DirectMapping<com.ftn.ues.emailclient.model.Message> {

    private String from;
    private String to;
    private String cc;
    private String bcc;
    private DateTime dateTime;
    private String subject;
    private String content;
    private Boolean unread;
    private Set<Attachment> attachments;
    private Set<Tag> tags;
    private Optional<Long> parentFolder;
    private Long account;

    public Message(com.ftn.ues.emailclient.model.Message object) {
        super(object);
        from = object.getFrom();
        to = object.getTo();
        cc = object.getCc();
        bcc = object.getBcc();
        dateTime = object.getDateTime();
        subject = object.getSubject();
        content = object.getContent();
        unread = object.getUnread();
        attachments = object.getAttachments().stream().map(Attachment::new).collect(Collectors.toSet());
        tags = object.getTags().stream().map(Tag::new).collect(Collectors.toSet());
        account = object.getAccount().getId();
    }

    @Override
    public com.ftn.ues.emailclient.model.Message getModelObject() {
        return com.ftn.ues.emailclient.model.Message.builder()
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
                .tags(tags.stream().map(Tag::getModelObject).collect(Collectors.toSet()))
                .account(Account.builder().id(account).build())
                .build();
    }
}
