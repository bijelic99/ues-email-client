package com.ftn.ues.emailclient.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;
import java.util.Set;

@Data
@SuperBuilder
public class Account extends Identifiable {
    @NonNull
    private String smtpAddress;
    @NonNull
    private Integer smtpPort;
    @NonNull
    private InServerType inServerType;
    @NonNull
    private String inServerAddress;
    @NonNull
    private Integer inServerPort;
    @NonNull
    private String username;
    @NonNull
    private String password;
    private Optional<String> displayName;
    @NonNull
    private User user;
    private Set<Folder> folders;
}
