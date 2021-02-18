package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account extends Identifiable {
    @NonNull
    @Column(name = "smtp_address", nullable = false)
    private String smtpAddress;

    @NonNull
    @Column(name = "smtp_port", nullable = false)
    private Integer smtpPort;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "in_server_type", nullable = false)
    private InServerType inServerType;

    @NonNull
    @Column(name = "in_server_address", nullable = false)
    private String inServerAddress;

    @NonNull
    @Column(name = "in_server_port", nullable = false)
    private Integer inServerPort;

    @NonNull
    @Column(nullable = false)
    private String username;

    @NonNull
    @Column(nullable = false)
    private String password;

    @NonNull
    @Column(nullable = false)
    private String mailAddress;

    @NonNull
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @NonNull
    @ManyToOne
    private User user;

    @NonNull
    @Builder.Default
    @OneToMany(mappedBy = "account", orphanRemoval = true)
    private Set<Folder> folders = new HashSet<>();

    @NonNull
    @Builder.Default
    @OneToMany(mappedBy = "account", orphanRemoval = true)
    private Set<Rule> accountRules = new HashSet<>();

    public String getSmtpAddress() {
        return smtpAddress;
    }

    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public InServerType getInServerType() {
        return inServerType;
    }

    public void setInServerType(InServerType inServerType) {
        this.inServerType = inServerType;
    }

    public String getInServerAddress() {
        return inServerAddress;
    }

    public void setInServerAddress(String inServerAddress) {
        this.inServerAddress = inServerAddress;
    }

    public Integer getInServerPort() {
        return inServerPort;
    }

    public void setInServerPort(Integer inServerPort) {
        this.inServerPort = inServerPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public void setFolders(Set<Folder> folders) {
        this.folders = folders;
    }

    public Set<Rule> getAccountRules() {
        return accountRules;
    }

    public void setAccountRules(Set<Rule> accountRules) {
        this.accountRules = accountRules;
    }
}
