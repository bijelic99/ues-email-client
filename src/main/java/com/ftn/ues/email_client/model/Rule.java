package com.ftn.ues.email_client.model;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "rule")
public class Rule extends Identifiable {
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name="_condition", nullable = false)
    private RuleCondition condition;

    @NonNull
    @Column(name = "rule_value")
    private String value;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleOperation operation;

    @ManyToOne
    @JoinColumn(name = "destination_folder")
    private Folder destinationFolder;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "account")
    private Account account;
}
