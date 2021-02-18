package com.ftn.ues.email_client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AttachmentRaw {
    private final String filename;
    private final String mimeType;
    private final byte[] data;

    public String getFilename() {
        return filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public byte[] getData() {
        return data;
    }
}
