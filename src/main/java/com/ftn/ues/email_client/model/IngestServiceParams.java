package com.ftn.ues.email_client.model;

import lombok.*;

@AllArgsConstructor
@Getter
public abstract class IngestServiceParams extends Object {
    @NonNull
    protected final Account account;
}
