package com.ftn.ues.email_client.model;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ImapIngestServiceParams extends IngestServiceParams{
    private Folder folder;

    public ImapIngestServiceParams(@NonNull Account account, Folder folder) {
        super(account);
        this.folder = folder;
    }
}
