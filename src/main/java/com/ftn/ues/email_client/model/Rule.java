package com.ftn.ues.email_client.model;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
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

    @Column(name = "destination_folder")
    private String destinationFolder;

    @NonNull
    @ManyToOne
    private Folder folder;
}
