package com.ftn.ues.email_client.model;

import lombok.*;

@AllArgsConstructor
public abstract class IngestServiceParams extends Object {
    @NonNull
    protected final Account account;

    public Account getAccount() {
        return account;
    }
}
