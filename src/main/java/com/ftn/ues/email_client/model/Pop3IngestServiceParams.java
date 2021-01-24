package com.ftn.ues.email_client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Pop3IngestServiceParams extends IngestServiceParams {
    public Pop3IngestServiceParams(@NonNull Account account) {
        super(account);
    }
}
