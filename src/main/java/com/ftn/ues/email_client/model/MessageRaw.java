package com.ftn.ues.email_client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joda.time.DateTime;

import java.util.List;

@AllArgsConstructor
@Getter
public class MessageRaw {
    private final String id;
    private final String from;
    private final String to;
    private final String cc;
    private final String bcc;
    private final DateTime dateTime;
    private final String subject;
    private final String content;
    private final Boolean unread;
    private final List<AttachmentRaw> rawAttachments;
}
