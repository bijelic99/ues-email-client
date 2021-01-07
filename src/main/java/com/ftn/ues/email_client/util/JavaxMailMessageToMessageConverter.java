package com.ftn.ues.email_client.util;

import com.ftn.ues.email_client.model.StoredDataWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joda.time.DateTime;

import javax.mail.*;
import javax.mail.internet.ContentType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public interface JavaxMailMessageToMessageConverter {
    static ParsedMessage convertToMessage(javax.mail.Message jMessage, String uid) throws MessagingException, IOException {

        String id = uid != null ? uid : UUID.randomUUID().toString();
        var fromAddresses = jMessage.getFrom();
        String from =
                fromAddresses != null ? Arrays.stream(fromAddresses)
                        .map(Address::toString)
                        .collect(Collectors.joining(", ")) : "";
        var toAddressed = jMessage.getRecipients(javax.mail.Message.RecipientType.TO);
        String to = toAddressed != null ? Arrays.stream(toAddressed)
                .map(Address::toString)
                .collect(Collectors.joining(", ")) : "";
        var ccAddresses = jMessage.getRecipients(Message.RecipientType.CC);
        String cc = ccAddresses != null ? Arrays.stream(ccAddresses)
                .map(Address::toString)
                .collect(Collectors.joining(", ")) : "";
        var bccAddresses = jMessage.getRecipients(Message.RecipientType.BCC);
        String bcc = bccAddresses != null ? Arrays.stream(bccAddresses)
                .map(Address::toString)
                .collect(Collectors.joining(", ")) : "";
        DateTime dateTime = new DateTime((jMessage.getSentDate() == null ? new Date() : jMessage.getSentDate()).getTime());
        String subject = jMessage.getSubject();
        List<Object> contents = parsePart(jMessage);
        var content = contents.stream()
                .filter(o -> o instanceof String)
                .map(o -> (String) o)
                .collect(Collectors.joining("\n"));
        var attachments = contents.stream()
                .filter(o -> o instanceof StoredDataWrapper)
                .map(o -> (StoredDataWrapper) o)
                .collect(Collectors.toList());

        return new ParsedMessage(id, from, to, cc, bcc, dateTime, subject, content, attachments);
    }

    private static List<Object> parsePart(Part part) throws MessagingException, IOException {
        var content = new ArrayList<>();
        var contentType = new ContentType(part.getContentType());
        // TODO possible problem with line below
        if (contentType.getPrimaryType().equals("text")) {
            content.add(part.getContent().toString());
        } else if (contentType.getPrimaryType().equals("multipart")) {
            Multipart body = (Multipart) part.getContent();
            for (int i = 0; i < body.getCount(); i++) {
                content.addAll(parsePart(body.getBodyPart(i)));
            }
        } else if (part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT) && part.getFileName() != null && !part.getFileName().isBlank()) {
            content.add(parseAttachment(part.getInputStream(), part.getFileName(), part.getContentType()));
        }
        return content;
    }

    private static StoredDataWrapper parseAttachment(InputStream inputStream, String fileName, String contentType) throws IOException {
        try (inputStream; ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            inputStream.transferTo(outputStream);
            outputStream.close();
            var attachmentType = Arrays.stream(contentType.split(";")).findFirst().orElseThrow();
            return new StoredDataWrapper(fileName, attachmentType, outputStream.toByteArray());
        }
    }

    @AllArgsConstructor
    @Getter
    class ParsedMessage {
        private final String id;
        private final String from;
        private final String to;
        private final String cc;
        private final String bcc;
        private final DateTime dateTime;
        private final String subject;
        private final String content;
        private final List<StoredDataWrapper> attachments;
    }
}
