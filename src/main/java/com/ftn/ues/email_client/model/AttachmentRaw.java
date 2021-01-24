package com.ftn.ues.email_client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AttachmentRaw {
    private final String filename;
    private final String mimeType;
    private final byte[] data;
}
