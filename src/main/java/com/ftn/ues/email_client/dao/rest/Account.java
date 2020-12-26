package com.ftn.ues.email_client.dao.rest;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.model.Identifiable;
import com.ftn.ues.email_client.model.InServerType;
import com.ftn.ues.email_client.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@NoArgsConstructor
public class Account extends DirectMapping<com.ftn.ues.email_client.model.Account> {

    public Account(com.ftn.ues.email_client.model.Account account){
        id = account.getId();
        smtpAddress = account.getSmtpAddress();
        smtpPort = account.getSmtpPort();
        inServerType = account.getInServerType();
        inServerAddress = account.getInServerAddress();
        inServerPort = account.getInServerPort();
        username = account.getUsername();
        password = account.getPassword();
        displayName = account.getDisplayName();
        user = account.getUser().getId();
        folders = account.getFolders().stream().map(Identifiable::getId).collect(Collectors.toSet());
    }

    @NonNull
    private Long id;

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

    @NonNull
    private String displayName;

    @NonNull
    private Long user;

    @NonNull
    @Builder.Default
    private Set<Long> folders = new HashSet<>();

    @Override
    public com.ftn.ues.email_client.model.Account getModelObject() {
        var usr = new User();
        usr.setId(user);
        return com.ftn.ues.email_client.model.Account.builder()
                .id(id)
                .smtpAddress(smtpAddress)
                .smtpPort(smtpPort)
                .inServerType(inServerType)
                .inServerAddress(inServerAddress)
                .inServerPort(inServerPort)
                .username(username)
                .password(password)
                .displayName(displayName)
                .user(usr)
                .folders(folders.stream().map(folder -> {
                    var f = new Folder();
                    f.setId(folder);
                    return f;
                }).collect(Collectors.toSet()))
                .build();
    }
}
