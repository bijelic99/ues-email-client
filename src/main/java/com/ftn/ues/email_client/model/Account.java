package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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
}
