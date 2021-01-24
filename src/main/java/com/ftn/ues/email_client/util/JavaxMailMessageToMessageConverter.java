package com.ftn.ues.email_client.util;

import com.ftn.ues.email_client.model.MessageRaw;
import com.ftn.ues.email_client.model.AttachmentRaw;
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

    static MessageRaw convertToRawMessage(javax.mail.Message jMessage, String uid) throws MessagingException, IOException {
        var id = uid != null ? uid : UUID.randomUUID().toString();
        var from = addressToCsv(jMessage.getFrom());
        var to = addressToCsv(jMessage.getRecipients(Message.RecipientType.TO));
        var cc = addressToCsv(jMessage.getRecipients(Message.RecipientType.CC));
        var bcc = addressToCsv(jMessage.getRecipients(Message.RecipientType.BCC));
        var dateTime = Optional.ofNullable(jMessage.getSentDate())
                .map(sentDate -> new DateTime(sentDate.getTime()))
                .orElse(DateTime.now());
        var subject = jMessage.getSubject();
        List<Object> contents = parsePart(jMessage);
        var content = contents.stream()
                .filter(o -> o instanceof String)
                .map(o -> (String) o)
                .collect(Collectors.joining("\n"));
        var attachments = contents.stream()
                .filter(o -> o instanceof AttachmentRaw)
                .map(o -> (AttachmentRaw) o)
                .collect(Collectors.toList());
        return new MessageRaw(id, from, to, cc, bcc, dateTime, subject, content, false, attachments);
    }

    static String addressToCsv(Address[] addresses) {
        return Optional.ofNullable(addresses)
                .stream()
                .flatMap(a ->
                        Arrays.stream(a)
                                .map(Address::toString))
                .collect(Collectors.joining(", "));
    }

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
                .filter(o -> o instanceof AttachmentRaw)
                .map(o -> (AttachmentRaw) o)
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

    private static AttachmentRaw parseAttachment(InputStream inputStream, String fileName, String contentType) throws IOException {
        try (inputStream; ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            inputStream.transferTo(outputStream);
            outputStream.close();
            var attachmentType = Arrays.stream(contentType.split(";")).findFirst().orElseThrow();
            return new AttachmentRaw(fileName, attachmentType, outputStream.toByteArray());
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
        private final List<AttachmentRaw> attachments;
    }
}
