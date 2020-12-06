package com.ftn.ues.emailclient.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Attachment extends Identifiable {
    private String path;
    private String mimeType;
    private String name;
    private Message message;
}
