package com.ftn.ues.email_client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joda.time.DateTime;

import java.util.List;

@AllArgsConstructor
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

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getCc() {
        return cc;
    }

    public String getBcc() {
        return bcc;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public Boolean getUnread() {
        return unread;
    }

    public List<AttachmentRaw> getRawAttachments() {
        return rawAttachments;
    }
}
