package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "attachment")
public class Attachment extends Identifiable {
    @NonNull
    @Column(nullable = false)
    private String path;

    @NonNull
    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @ManyToOne
    private Message message;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return Attachment.builder()
                .id(this.id)
                .path(this.path)
                .mimeType(this.mimeType)
                .name(this.name)
                .message(this.message);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
